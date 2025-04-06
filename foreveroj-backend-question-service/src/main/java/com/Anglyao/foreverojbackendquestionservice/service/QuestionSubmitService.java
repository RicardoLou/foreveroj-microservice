package com.Anglyao.foreverojbackendquestionservice.service;

import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.Anglyao.foreverojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.Anglyao.foreverojbackendmodel.model.entity.QuestionSubmit;
import com.Anglyao.foreverojbackendmodel.model.entity.User;
import com.Anglyao.foreverojbackendmodel.model.vo.QuestionSubmitVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ricardo
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2025-03-24 22:48:21
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);


    /**
     * 获取题目提交封装
     */
    void someLogic();
}
