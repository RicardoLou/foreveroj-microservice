package com.Anglyao.foreverojbackendserviceclient.judge;


import com.Anglyao.foreverojbackendserviceclient.judge.strategy.DefaultJudgeStrategy;
import com.Anglyao.foreverojbackendserviceclient.judge.strategy.JavaLanguageJudgeStrategy;
import com.Anglyao.foreverojbackendserviceclient.judge.strategy.JudgeContext;
import com.Anglyao.foreverojbackendserviceclient.judge.strategy.JudgeStrategy;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language) || "Java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
