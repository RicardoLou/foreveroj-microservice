package com.Anglyao.foreverojbackendserviceclient.service;

import com.Anglyao.foreverojbackendmodel.common.BaseResponse;
import com.Anglyao.foreverojbackendmodel.common.ErrorCode;
import com.Anglyao.foreverojbackendmodel.exception.BusinessException;
import com.Anglyao.foreverojbackendmodel.model.dto.user.UserSubmitNumber;
import com.Anglyao.foreverojbackendmodel.model.entity.User;
import com.Anglyao.foreverojbackendmodel.model.enums.UserRoleEnum;
import com.Anglyao.foreverojbackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.Anglyao.foreverojbackendmodel.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/RicardoLou">RicardoLou</a>
 */
@FeignClient(name = "foreveroj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {
    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    @GetMapping("/get/id")
    User getUserById(@RequestParam("userId") Long id);

    /**
     * 根据id获取用户列表
     * @param idList
     * @return
     */
    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList") Collection<Long> idList);

    /**
     * 提交数增加
     * @param userId
     * @return
     */
    @PostMapping("/submit/increase")
    BaseResponse<Boolean> increaseSubmitCount(@RequestParam("userId") Long userId);

    /**
     * 通过数增加
     */
    @PostMapping("/accept/increase")
    BaseResponse<Boolean> increaseAcceptCount(@RequestParam("userId") Long userId);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */

    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 查询每一个人的个人信息和提交次数
     */
    @GetMapping("/get/all/submit")
    BaseResponse<List<UserSubmitNumber>> getAllUserSubmit();
}
