package com.Anglyao.foreverojbackendserviceclient.judge;

import cn.hutool.json.JSONUtil;
import com.Anglyao.foreverojbackendmodel.common.BaseResponse;
import com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.CodeSandbox;
import com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.CodeSandboxFactory;
import com.Anglyao.foreverojbackendserviceclient.judge.codesandbox.CodeSandboxProxy;
import com.Anglyao.foreverojbackendserviceclient.judge.strategy.JudgeContext;
import com.Anglyao.foreverojbackendserviceclient.service.QuestionFeignClient;
import com.Anglyao.foreverojbackendmodel.common.ErrorCode;
import com.Anglyao.foreverojbackendmodel.exception.BusinessException;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeRequest;
import com.Anglyao.foreverojbackendmodel.model.codeSandbox.ExecuteCodeResponse;
import com.Anglyao.foreverojbackendmodel.model.dto.question.JudgeCase;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.JudgeInfo;
import com.Anglyao.foreverojbackendmodel.model.entity.Question;
import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import com.Anglyao.foreverojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.Anglyao.foreverojbackendserviceclient.service.UserFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService{
    @Value("${codeSandbox.type:example}")
    String type;
    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 判题服务
     * @param questionSubmitID
     * @return
     */
    @Override
    public QuestionSubmit doJudge(Long questionSubmitID) {
        // 传入 提交 题目ID，通过ID获取到题目相关信息、提交信息
        // 调用沙箱，编译执行代码
        // 根据沙箱返回结果设置题目判题状态反馈给用户
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitID);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 只处理等待中的状态
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目已提交");
        }
        // 更新状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitID);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean result = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态更新错误");
        }
        // 调用沙箱
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type); // 从配置中获取
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        String judgeCaseStr = question.getJudgeCase(); // 当前question的答案
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        // 获取到用例中的输入部分
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outList = executeCodeResponse.getOutList(); // 用户代码运行的输出

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 修改数据库判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitID);
        // 判题状态成功
        // 更新题目的通过数和用户的通过数
        // 判断是否通过所有用例
        if ("Accepted".equals(judgeInfo.getMessage())) {
            // 更新题目的通过数和用户的通过数
            BaseResponse<Boolean> response = userFeignClient.increaseAcceptCount(questionSubmit.getUserId());
            if (response == null || Boolean.FALSE.equals(response.getData())) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "增加用户提交次数失败");
            }

            BaseResponse<Boolean> booleanBaseResponse = questionFeignClient.increaseAccept(questionId);
            if (booleanBaseResponse == null || Boolean.FALSE.equals(booleanBaseResponse.getData())) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "增加题目提交次数失败");
            }

            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        } else {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        }

        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        result = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态更新错误");
        }
        return questionFeignClient.getQuestionSubmitById(questionSubmitID);
    }
}
