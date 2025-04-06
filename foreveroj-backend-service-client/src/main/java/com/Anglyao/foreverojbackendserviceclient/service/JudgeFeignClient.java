package com.Anglyao.foreverojbackendserviceclient.service;


import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "foreveroj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);

}
