package com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.impl;


import com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.CodeSandbox;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeResponse;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.Anglyao.foreverojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.Anglyao.foreverojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
