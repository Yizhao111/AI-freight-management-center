package vip.penint.cloud.server.ques.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.ques.entity.Exam;
import vip.penint.cloud.server.ques.entity.ExamAnswer;
import vip.penint.cloud.server.ques.entity.vo.AnswerListVO;
import vip.penint.cloud.server.ques.entity.vo.MyAnswerVO;

/**
 * <p>
 * 问卷表 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
public interface IExamService extends IService<Exam> {

    void addExam(Exam exam) throws PenintException;

    void updateTheme(Exam exam);

    IPage<Exam> selectExamList(Exam exam, QueryRequest queryRequest);

    Exam selectExamInfo(String examId) throws PenintException;

    void delExam(String examId) throws PenintException;

    void editStatus(String examId, String status);


    Exam previewInfo(String examId);

    IPage<Exam> myList(Exam exam, QueryRequest queryRequest);

    IPage<MyAnswerVO> myAnswer(Exam exam, QueryRequest queryRequest);

    ExamAnswer myAnswerInfo(String answerId);

    IPage<AnswerListVO> examAnswerList(String examId, QueryRequest queryRequest);

    JSONObject examAnswerStat(String examId, String quId);

}
