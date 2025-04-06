package com.Anglyao.foreverojbackendquestionservice.controller.inner;

import com.Anglyao.foreverojbackendmodel.common.BaseResponse;
import com.Anglyao.foreverojbackendmodel.model.entity.Question;
import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import com.Anglyao.foreverojbackendquestionservice.service.QuestionService;
import com.Anglyao.foreverojbackendquestionservice.service.QuestionSubmitService;
import com.Anglyao.foreverojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;
    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    public BaseResponse<Boolean> increaseAccept(long questionId) {
        questionService.update()
                .eq("id", questionId)
//                .gt("acceptedNum", 0)
                .setSql("acceptedNum = acceptedNum + 1")
                .update();
        return new BaseResponse<>(0, true);
    }
}
