package com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.impl;


import com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.CodeSandbox;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱
 * 调用第三方写好的代码沙箱
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方");
        return null;
    }
}
