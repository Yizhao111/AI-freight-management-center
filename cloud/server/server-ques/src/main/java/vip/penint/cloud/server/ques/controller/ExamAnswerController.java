package vip.penint.cloud.server.ques.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.ques.entity.ExamAnswer;
import vip.penint.cloud.server.ques.service.IExamAnswerService;

/**
 * <p>
 * 问卷回答表 前端控制器
 * </p>
 *
 * @author Penint
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/examAnswer")
public class ExamAnswerController {


    @Autowired
    private IExamAnswerService examAnswerService;


    @PostMapping
    public AjaxResult add(@RequestBody ExamAnswer examAnswer) throws PenintException {

        return examAnswerService.add(examAnswer);
    }
}

