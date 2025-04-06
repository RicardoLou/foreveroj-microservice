package com.Anglyao.foreverojbackendserviceclient.service;

import com.Anglyao.foreverojbackendmodel.common.BaseResponse;
import com.Anglyao.foreverojbackendmodel.model.entity.Question;
import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author ricardo
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-03-24 22:35:49
*/
@FeignClient(name = "foreveroj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);

    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    /**
     * 更新题目通过数
     */
    @PostMapping("/accept/increase")
    BaseResponse<Boolean> increaseAccept(@RequestParam("questionId") long questionId);
}
