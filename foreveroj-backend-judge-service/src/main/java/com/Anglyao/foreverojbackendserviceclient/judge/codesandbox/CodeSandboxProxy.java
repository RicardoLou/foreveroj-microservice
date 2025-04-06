package com.Anglyao.foreverojbackendserviceclient.judge.codesandbox;


import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox{

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息:" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息:" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
