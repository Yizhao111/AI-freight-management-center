package vip.penint.cloud.server.ques.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.ques.entity.*;
import vip.penint.cloud.server.ques.entity.vo.AnswerListVO;
import vip.penint.cloud.server.ques.entity.vo.MyAnswerVO;
import vip.penint.cloud.server.ques.feign.IRemoteUserService;
import vip.penint.cloud.server.ques.mapper.IExamMapper;
import vip.penint.cloud.server.ques.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 问卷表 服务实现类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
@Service
public class ExamServiceImpl extends ServiceImpl<IExamMapper, Exam> implements IExamService {


    @Autowired
    private IQuService quService;

    @Autowired
    private IQuAnswerService quAnswerService;

    @Autowired
    private IExamQuService examQuService;

    @Autowired
    private IExamQuAnswerService examQuAnswerService;

    @Autowired
    private IRemoteUserService remoteUserService;

    @Autowired
    private IExamAnswerService examAnswerService;

    /**
     * 展示的选项，ABC这样
     */
    private static List<String> ABC = Arrays.asList(new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X"
            , "Y", "Z"
    });


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addExam(Exam exam) throws PenintException {
        exam.setStartUser(PenintUtil.getCurrentUser().getUserId());
        if (exam.getTimeLimit()) {
            List<String> dateValues = exam.getDateValues();
            exam.setStartTime(LocalDate.parse(dateValues.get(0)));
            exam.setStopTime(LocalDate.parse(dateValues.get(1)));
        }

        if (exam.getOpenType() == 2) {
            List<SysUser> selectUserList = exam.getSelectUserList();
            JSONArray arr = new JSONArray();
            for (SysUser sysUser : selectUserList) {
                Long userId = sysUser.getUserId();
                arr.add(userId);
            }
            exam.setUserIdStr(arr.toJSONString());

        } else {
            exam.setUserIdStr(null);
        }
        if (StringUtils.isNotEmpty(exam.getId())) {
            if (this.getById(exam.getId()).getStatus().equals("1")) {
                throw new PenintException("该问卷已发布不支持修改");
            }
        }
        this.saveOrUpdate(exam);

        // 修改的时候删除两张子表的数据
        examQuService.removeByExamId(exam.getId());
        examQuAnswerService.removeByExamId(exam.getId());
        // 已选择的题目
        List<Qu> quList = exam.getSelectList();
        saveQu(exam.getId(), quList);

    }

    @Override
    public void updateTheme(Exam exam) {
        this.updateById(exam);
    }

    /**
     * 保存问卷题目
     */
    private void saveQu(String examId, List<Qu> quList) {
        Integer i = 0;
        List<ExamQu> batchQuList = new ArrayList<>();
        List<ExamQuAnswer> batchAnswerList = new ArrayList<>();

        for (Qu qu : quList) {
            Qu quInfo = quService.getInfoById(qu.getId());
            ExamQu examQu = new ExamQu();
            examQu.setExamId(examId);
            examQu.setQuId(quInfo.getId());
            examQu.setQuType(quInfo.getQuType());
            examQu.setSort(i);
            i++;
            batchQuList.add(examQu);

            // 获取候选答案
            List<QuAnswer> quAnswers = quAnswerService.getByQuId(qu.getId());

            Integer ii = 0;
            for (QuAnswer quAnswer : quAnswers) {
                ExamQuAnswer examQuAnswer = new ExamQuAnswer();
                examQuAnswer.setExamId(examId);
                examQuAnswer.setAnswerId(quAnswer.getId());
                examQuAnswer.setQuId(qu.getId());
                examQuAnswer.setSort(ii);
                examQuAnswer.setAbc(ABC.get(ii));
                ii++;
                batchAnswerList.add(examQuAnswer);

            }
        }

        examQuService.saveBatch(batchQuList);
        examQuAnswerService.saveBatch(batchAnswerList);
    }

    @Override
    public IPage<Exam> selectExamList(Exam exam, QueryRequest queryRequest) {
        IPage<Exam> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotNull(exam.getOpenType()), Exam::getOpenType, exam.getOpenType())
                .like(StringUtils.isNotEmpty(exam.getTitle()), Exam::getTitle, exam.getTitle())
                .eq(StringUtils.isNotNull(exam.getStartUser()), Exam::getStartUser, exam.getStartUser())
                .in(ObjectUtil.isNotNull(exam.getUids()) && exam.getUids().size() > 0, Exam::getStartUser, exam.getUids())
                .orderByDesc(Exam::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Exam selectExamInfo(String examId) throws PenintException {
        Exam exam = this.getById(examId);
        if (exam.getTimeLimit()) {
            // 处理前端时间
            ArrayList<String> dateValues = new ArrayList<String>() {{
                add(exam.getStartTime().toString());
                add(exam.getStopTime().toString());
            }};
            exam.setDateValues(dateValues);
        }
        // 处理题目
        exam.setSelectList(examQuService.selectByExamId(examId));
        // 处理答者
        if (exam.getOpenType() == 2) {
            String userIdStr = exam.getUserIdStr();
            userIdStr = userIdStr.substring(1, userIdStr.length() - 1);
            String[] arr = userIdStr.split(",");

            ArrayList<SysUser> list = new ArrayList<>(arr.length);

            for (String s : arr) {
                SysUser user = (SysUser) remoteUserService.infoUser(Long.valueOf(s)).getData();
                list.add(user);
            }
            exam.setSelectUserList(list);
        }
        return exam;
    }

    @Override
    public void delExam(String examId) throws PenintException {
        if (this.getById(examId).getStatus().equals("1")) {
            throw new PenintException("该问卷已发布不支持删除");
        }
        this.removeById(examId);
        examQuService.removeByExamId(examId);
        examQuAnswerService.removeByExamId(examId);
    }

    @Override
    public void editStatus(String examId, String status) {
        Exam exam = new Exam();
        exam.setId(examId);
        exam.setStatus(status);
        this.updateById(exam);
    }

    @Override
    public Exam previewInfo(String examId) {
        Exam exam = this.getById(examId);
        // 处理题目
        exam.setSelectList(examQuService.selectAnswerByExamId(examId));
        return exam;
    }

    @Override
    public IPage<Exam> myList(Exam exam, QueryRequest queryRequest) {
        IPage<Exam> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);

        return baseMapper.myList(exam, PenintUtil.getCurrentUser().getUserId(), page);
    }

    @Override
    public IPage<MyAnswerVO> myAnswer(Exam exam, QueryRequest queryRequest) {
        IPage<MyAnswerVO> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);

        return baseMapper.myAnswer(exam, PenintUtil.getCurrentUser().getUserId(), page);
    }

    @Override
    public ExamAnswer myAnswerInfo(String answerId) {
        return examAnswerService.getById(answerId);
    }

    @Override
    public IPage<AnswerListVO> examAnswerList(String examId, QueryRequest queryRequest) {
        IPage<MyAnswerVO> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        IPage<AnswerListVO> answerListVOIPage = examAnswerService.examAnswerList(examId, page);
        answerListVOIPage.getRecords().forEach(e-> {
            try {
                Object data = remoteUserService.infoUser(Long.valueOf(e.getUserId())).getData();
                JSONObject remoteJson = JSONObject.parseObject(JSONObject.toJSONString(data));
                e.setNickName(remoteJson.getString("nickName"));
            } catch (PenintException ex) {
                throw new RuntimeException(ex);
            }

        });
        return answerListVOIPage;
    }

    @Override
    public JSONObject examAnswerStat(String examId, String quId) {
        List<ExamAnswer> examAnswers = examAnswerList(examId);
        JSONArray arr = new JSONArray();
        for (ExamAnswer e : examAnswers) {
            JSONObject data = e.getData();
            JSONArray selectList = data.getJSONArray("selectList");
            for (int i = 0; i < selectList.size(); i++) {
                JSONObject qu = selectList.getJSONObject(i);
                if (qu.getString("id").equals(quId)) {
                    arr.add(qu);
                }

            }
        }

        JSONObject stat = new JSONObject();
        for (int i = 0; i < arr.size(); i++) {
            JSONArray answerList = arr.getJSONObject(i).getJSONArray("answerList");
            for (int j = 0; j < answerList.size(); j++) {
                JSONObject data = answerList.getJSONObject(j);
                Boolean checked = data.getBoolean("checked");
                if (checked != null && checked) {
                    if (StringUtils.isNotNull(stat.getString(String.valueOf(j)))) {
                        Integer num = stat.getInteger(String.valueOf(j));
                        stat.put(String.valueOf(j), num + 1);
                    } else {
                        stat.put(String.valueOf(j), 1);
                    }
                } else {
                    if (StringUtils.isNotNull(stat.getString(String.valueOf(j)))) {
                        Integer num = stat.getInteger(String.valueOf(j));
                        stat.put(String.valueOf(j), num + 0);
                    } else {
                        stat.put(String.valueOf(j), 0);
                    }
                }
            }
        }

        JSONObject result = new JSONObject();
        result.put("stat", stat);
        result.put("all", arr.size());
        return result;
    }

    private List<ExamAnswer> examAnswerList(String examId) {

        LambdaQueryWrapper<ExamAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamAnswer::getExamId, examId);
        return examAnswerService.list(wrapper);
    }
}
