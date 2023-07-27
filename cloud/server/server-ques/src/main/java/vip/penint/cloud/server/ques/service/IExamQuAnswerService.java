package vip.penint.cloud.server.ques.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.server.ques.entity.ExamQuAnswer;

/**
 * <p>
 * 问卷题目备选答案 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
public interface IExamQuAnswerService extends IService<ExamQuAnswer> {

    void removeByExamId(String examId);
}
