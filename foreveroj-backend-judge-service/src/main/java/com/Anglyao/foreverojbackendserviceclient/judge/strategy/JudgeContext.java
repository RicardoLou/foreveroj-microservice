package com.Anglyao.foreverojbackendserviceclient.judge.strategy;


import com.Anglyao.foreverojbackendmodel.model.dto.question.JudgeCase;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.Anglyao.foreverojbackendmodel.model.entity.Question;
import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 判题策略中需要的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit questionSubmit;
}
