package vip.penint.cloud.server.ques.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.server.ques.entity.Exam;
import vip.penint.cloud.server.ques.entity.ExamAnswer;
import vip.penint.cloud.server.ques.entity.vo.AnswerListVO;
import vip.penint.cloud.server.ques.entity.vo.MyAnswerVO;
import vip.penint.cloud.server.ques.mapper.IExamAnswerMapper;
import vip.penint.cloud.server.ques.service.IExamAnswerService;
import vip.penint.cloud.server.ques.service.IExamService;
import vip.penint.cloud.server.ques.service.IQuService;
import vip.penint.cloud.server.ques.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷回答表 服务实现类
 * </p>
 *
 * @author Penint
 * @since 2022-11-09
 */
@Service
public class ExamAnswerServiceImpl extends ServiceImpl<IExamAnswerMapper, ExamAnswer> implements IExamAnswerService {


    @Autowired
    private IExamService examService;

    @Autowired
    private IQuService quService;

    @Override
    public AjaxResult add(ExamAnswer examAnswer) throws PenintException {
        // 判断该场问卷一个人可以回答几次
        String examId = examAnswer.getExamId();
        Exam exam = examService.selectExamInfo(examId);
        // 判断问卷状态
        if (!exam.getStatus().equals("1")) {
            return AjaxResult.error("问卷状态不正确");
        }

        // 如果限时，需要判断结束时间
        if (exam.getTimeLimit()) {
            LocalTime start = LocalTime.parse("00:00:00");
            LocalTime end = LocalTime.parse("23:59:59");
            LocalDate startTime = exam.getStartTime();
            LocalDate stopTime = exam.getStopTime();
            LocalDateTime timeStart = startTime.atTime(start);
            LocalDateTime timeEnd = stopTime.atTime(end);

            LocalDateTime now = LocalDateTime.now();
            boolean b = belongCalendar(now, timeStart, timeEnd);
            if (!b) {
                return AjaxResult.error("该问卷时间不在此刻");
            }
        }

        // 判断问卷是否公开，如非公开，则判断当前用户是否可答
        if (exam.getOpenType() == 2) {
            // 如果指定答者，则需要获取答者
            List<SysUser> selectUserList = exam.getSelectUserList();
            boolean flag = false;
            for (SysUser sysUser : selectUserList) {
                if (sysUser.getUserId().equals(PenintUtil.getCurrentUser().getUserId())) {
                    flag = true;
                }
            }
            if (!flag) {
                return AjaxResult.error("该问卷为指定答者，您不是指定的答者不能答题！");
            }
        }

        // 判断答者答过几次
        List<ExamAnswer> examAnswers = this.selectByUidAndExamId(examId);

        if (examAnswers.size() >= exam.getNum()) {
            return AjaxResult.error("该问卷最多答题：" + exam.getNum() + "次，您已经答过：" + examAnswers.size() + "次了！");
        }
        examAnswer.setUserId(Math.toIntExact(PenintUtil.getCurrentUser().getUserId()));
        baseMapper.insert(examAnswer);
        return AjaxResult.success();

    }

    @Override
    public List<ExamAnswer> selectByUidAndExamId(String examId) {
        LambdaQueryWrapper<ExamAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamAnswer::getUserId, PenintUtil.getCurrentUser().getUserId()).eq(ExamAnswer::getExamId, examId);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(LocalDateTime nowTime, LocalDateTime beginTime, LocalDateTime endTime) {

        Date now1 = DateUtils.LocalDateTimeToDate(nowTime);
        Date begin1 = DateUtils.LocalDateTimeToDate(beginTime);
        Date end1 = DateUtils.LocalDateTimeToDate(endTime);


        if (now1.getTime() == begin1.getTime()
                || now1.getTime() == end1.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(now1);

        Calendar begin = Calendar.getInstance();
        begin.setTime(begin1);

        Calendar end = Calendar.getInstance();
        end.setTime(end1);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IPage<AnswerListVO> examAnswerList(String examId, IPage<MyAnswerVO> page) {
        return baseMapper.examAnswerList(examId, page);
    }

    @Override
    public List<Map<String, Object>> allQues() {
        return baseMapper.allQues();
    }

    @Override
    public JSONObject answerCount() {

        JSONObject data = new JSONObject();
        // 今日题库新增数量
        Integer jrtk = quService.selectListByDay(DateUtil.today());
        data.put("jrtk", jrtk);
        // 昨日题库新增数量
        Integer zrtk = quService.selectListByDay(DateUtil.yesterday().toDateStr());
        data.put("zrtk", zrtk);
        // 今日答题数量
        Integer jrdt = baseMapper.selectListByDay(DateUtil.today());
        data.put("jrdt", jrdt);
        // 昨日答题数量
        Integer zrdt = baseMapper.selectListByDay(DateUtil.yesterday().toDateStr());
        data.put("zrdt", zrdt);
        return data;
    }


    public static void main(String[] args) {
        System.out.println(DateUtil.yesterday().toDateStr());
    }
}
