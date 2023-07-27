package vip.penint.cloud.server.ques.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.penint.cloud.server.ques.entity.ExamQu;
import vip.penint.cloud.server.ques.entity.Qu;
import vip.penint.cloud.server.ques.entity.QuAnswer;
import vip.penint.cloud.server.ques.mapper.IExamQuMapper;
import vip.penint.cloud.server.ques.service.IExamQuService;
import vip.penint.cloud.server.ques.service.IExamService;
import vip.penint.cloud.server.ques.service.IQuAnswerService;
import vip.penint.cloud.server.ques.service.IQuService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 试卷考题 服务实现类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
@Service
public class ExamQuServiceImpl extends ServiceImpl<IExamQuMapper, ExamQu> implements IExamQuService {

    @Autowired
    private IQuService quService;

    @Autowired
    private IQuAnswerService quAnswerService;

    @Autowired
    private IExamService examService;

    @Override
    public List<Qu> selectByExamId(String examId) {

        LambdaQueryWrapper<ExamQu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamQu::getExamId, examId);
        List<ExamQu> examQus = baseMapper.selectList(wrapper);
        List<Qu> qus = new ArrayList<>();
        for (ExamQu examQu : examQus) {
            Qu qu = quService.getById(examQu.getQuId());
            qus.add(qu);
        }
        return qus;
    }

    @Override
    public List<Qu> selectAnswerByExamId(String examId) {
        LambdaQueryWrapper<ExamQu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamQu::getExamId, examId);
        List<ExamQu> examQus = baseMapper.selectList(wrapper);
        List<Qu> qus = new ArrayList<>();
        for (ExamQu examQu : examQus) {
            Qu qu = quService.getById(examQu.getQuId());
            JSONObject stat = examService.examAnswerStat(examId, examQu.getQuId());
            JSONObject obj = stat.getJSONObject("stat");
            List<QuAnswer> answerList = quAnswerService.getByQuId(qu.getId());
            for (int i = 0; i < answerList.size(); i++) {
                answerList.get(i).setCheckNum(obj.getInteger(String.valueOf(i)));
                answerList.get(i).setAll(stat.getInteger("all"));
            }
            qu.setAnswerList(answerList);
            qus.add(qu);
            qu.setStat(stat);


        }
        return qus;
    }

    @Override
    public void removeByExamId(String examId) {
        LambdaQueryWrapper<ExamQu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamQu::getExamId, examId);
        baseMapper.delete(wrapper);
    }
}
