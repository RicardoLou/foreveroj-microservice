package com.Anglyao.foreverojbackendserviceclient.judge;


import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {
    /**
     * 判题服务
     * @param questionSubmitID
     * @return
     */
    QuestionSubmit doJudge(Long questionSubmitID);
}
