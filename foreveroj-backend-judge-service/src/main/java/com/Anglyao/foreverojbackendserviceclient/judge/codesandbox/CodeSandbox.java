package com.Anglyao.foreverojbackendserviceclient.judge.codesandbox;


import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeResponse;

/**
 * 代码沙箱的定义
 *
 */
public interface CodeSandbox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
