package vip.penint.cloud.server.ques.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.penint.cloud.server.ques.entity.ExamQuAnswer;
import vip.penint.cloud.server.ques.mapper.IExamQuAnswerMapper;
import vip.penint.cloud.server.ques.service.IExamQuAnswerService;

/**
 * <p>
 * 问卷题目备选答案 服务实现类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
@Service
public class ExamQuAnswerServiceImpl extends ServiceImpl<IExamQuAnswerMapper, ExamQuAnswer> implements IExamQuAnswerService {

    @Override
    public void removeByExamId(String examId) {
        LambdaQueryWrapper<ExamQuAnswer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamQuAnswer::getExamId, examId);
        baseMapper.delete(wrapper);
    }
}
