package com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.Anglyao.foreverojbackendmodel.common.ErrorCode;
import com.Anglyao.foreverojbackendmodel.exception.BusinessException;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeResponse;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.CodeSandbox;

/**
 * 远程代码沙箱
 *
 */
public class RemoteCodeSandbox implements CodeSandbox {
    // 鉴权请求头
    private static final String AUTH_REQUEST_HEADER = "auth";
    // 密钥
    private static final String AUTH_REQUEST_SECRET = "1900";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8083/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StrUtil.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"remote sandbox error, message " + responseStr);
        }
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        if (responseStr.contains("编译错误")) {
            JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage("编译错误");
            executeCodeResponse.setJudgeInfo(judgeInfo);
            return executeCodeResponse;
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
