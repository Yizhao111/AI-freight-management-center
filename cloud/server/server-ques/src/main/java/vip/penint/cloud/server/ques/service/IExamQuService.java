package vip.penint.cloud.server.ques.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.server.ques.entity.ExamQu;
import vip.penint.cloud.server.ques.entity.Qu;

import java.util.List;

/**
 * <p>
 * 试卷考题 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
public interface IExamQuService extends IService<ExamQu> {

    List<Qu> selectByExamId(String examId);
    List<Qu> selectAnswerByExamId(String examId);

    void removeByExamId(String examId);
}
