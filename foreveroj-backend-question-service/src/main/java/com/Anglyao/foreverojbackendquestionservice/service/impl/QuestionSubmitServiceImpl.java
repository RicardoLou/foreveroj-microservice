package com.Anglyao.foreverojbackendquestionservice.service.impl;

import com.Anglyao.foreverojbackendmodel.common.BaseResponse;
import com.Anglyao.foreverojbackendmodel.common.ErrorCode;
import com.Anglyao.foreverojbackendmodel.constant.CommonConstant;
import com.Anglyao.foreverojbackendmodel.exception.BusinessException;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.Anglyao.foreverojbackendmodel.model.dto.user.UserSubmitNumber;
import com.Anglyao.foreverojbackendmodel.model.entity.Question;
import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import com.Anglyao.foreverojbackendmodel.model.entity.User;
import com.Anglyao.foreverojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.Anglyao.foreverojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.Anglyao.foreverojbackendmodel.model.vo.QuestionSubmitVO;
import com.Anglyao.foreverojbackendmodel.utils.SqlUtils;
import com.Anglyao.foreverojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.Anglyao.foreverojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.Anglyao.foreverojbackendquestionservice.service.QuestionService;
import com.Anglyao.foreverojbackendquestionservice.service.QuestionSubmitService;
import com.Anglyao.foreverojbackendserviceclient.service.JudgeFeignClient;
import com.Anglyao.foreverojbackendserviceclient.service.UserFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ricardo
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2025-03-24 22:48:21
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;

    @Override
    public void someLogic() {
        BaseResponse<List<UserSubmitNumber>> response = userFeignClient.getAllUserSubmit();
        List<UserSubmitNumber> userSubmitList = response.getData();
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        // 更新当前用户提交次数
        BaseResponse<Boolean> response = userFeignClient.increaseSubmitCount(userId);

        if (response == null || Boolean.FALSE.equals(response.getData())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "增加用户提交次数失败");
        }
        // 更新题目的提交数
        questionService.update()
                .eq("id", questionId)
                .setSql("submitNum = submitNum + 1")
                .update();
        Long id = questionSubmit.getId();
        // 发送消息
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(id));
//        CompletableFuture.runAsync(() -> {
//            judgeFeignClient.doJudge(id);
//        });
        return questionSubmit.getId();
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}