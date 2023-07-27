package vip.penint.cloud.server.ques.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.ques.entity.ExamAnswer;
import vip.penint.cloud.server.ques.entity.vo.AnswerListVO;
import vip.penint.cloud.server.ques.entity.vo.MyAnswerVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷回答表 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-09
 */
public interface IExamAnswerService extends IService<ExamAnswer> {

    AjaxResult add(ExamAnswer examAnswer) throws PenintException;

    List<ExamAnswer> selectByUidAndExamId(String examId);

    IPage<AnswerListVO> examAnswerList(String examId, IPage<MyAnswerVO> page);

    List<Map<String,Object>> allQues();

    JSONObject answerCount();
}
