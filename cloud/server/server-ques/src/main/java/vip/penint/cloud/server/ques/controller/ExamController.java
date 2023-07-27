package vip.penint.cloud.server.ques.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.ques.entity.Exam;
import vip.penint.cloud.server.ques.service.IExamService;

/**
 * <p>
 * 问卷表 前端控制器
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private IExamService examService;


    /**
     * 新增问卷
     */
    @PostMapping("/addExam")
    public AjaxResult addExam(@RequestBody Exam exam) throws PenintException {
        examService.addExam(exam);
        return AjaxResult.success();
    }

    /**
     * 修改主题
     */
    @PutMapping("/updateTheme")
    public AjaxResult updateTheme(@RequestBody Exam exam) {
        examService.updateTheme(exam);
        return AjaxResult.success();
    }


    /**
     * 查询问卷列表
     */
    @GetMapping("/list")
    public AjaxResult list(Exam exam, QueryRequest queryRequest) {
        return AjaxResult.success(examService.selectExamList(exam, queryRequest));

    }

    /**
     * 查询问卷详情
     */
    @GetMapping("/info/{examId}")
    public AjaxResult info(@PathVariable String examId) throws PenintException {
        return AjaxResult.success(examService.selectExamInfo(examId));
    }

    /**
     * 删除
     */
    @DeleteMapping("/del/{examId}")
    public AjaxResult del(@PathVariable String examId) throws PenintException {
        examService.delExam(examId);
        return AjaxResult.success();
    }


    /**
     * 状态修改
     */
    @PutMapping("/editStatus/{examId}/{status}")
    public AjaxResult editStatus(@PathVariable String examId, @PathVariable String status) {
        examService.editStatus(examId, status);
        return AjaxResult.success();
    }

    /**
     * 问卷详情
     */
    @GetMapping("/previewInfo/{examId}")
    public AjaxResult previewInfo(@PathVariable String examId) {
        return AjaxResult.success(examService.previewInfo(examId));
    }

    /**
     * 获取我可以回答的问卷列表
     */
    @GetMapping("/myList")
    public AjaxResult myList(Exam exam, QueryRequest queryRequest) {
        return AjaxResult.success(examService.myList(exam, queryRequest));
    }

    /**
     * 获取我已回答的列表
     */
    @GetMapping("/myAnswer")
    public AjaxResult myAnswer(Exam exam, QueryRequest queryRequest) {
        return AjaxResult.success(examService.myAnswer(exam, queryRequest));
    }

    /**
     * 获取我回答的详情
     */
    @GetMapping("/myAnswerInfo/{answerId}")
    public AjaxResult myAnswerInfo(@PathVariable String answerId) {
        return AjaxResult.success(examService.myAnswerInfo(answerId));
    }

    /**
     * 获取某个问卷答卷列表
     */
    @GetMapping("/examAnswerList/{examId}")
    public AjaxResult examAnswerList(@PathVariable String examId, QueryRequest queryRequest) {
        return AjaxResult.success(examService.examAnswerList(examId, queryRequest));
    }


    @GetMapping("/examAnswerStat/{examId}/{quId}")
    public AjaxResult examAnswerStat(@PathVariable String examId, @PathVariable String quId) {
        return AjaxResult.success(examService.examAnswerStat(examId, quId));
    }

}

