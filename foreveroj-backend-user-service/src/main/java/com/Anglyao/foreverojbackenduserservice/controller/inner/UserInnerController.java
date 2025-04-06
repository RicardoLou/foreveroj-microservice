package com.Anglyao.foreverojbackenduserservice.controller.inner;

import com.Anglyao.foreverojbackendmodel.common.BaseResponse;
import com.Anglyao.foreverojbackendmodel.model.dto.user.UserSubmitNumber;
import com.Anglyao.foreverojbackendmodel.model.entity.User;
import com.Anglyao.foreverojbackendserviceclient.service.UserFeignClient;
import com.Anglyao.foreverojbackenduserservice.mapper.UserMapper;
import com.Anglyao.foreverojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    @Resource UserMapper userMapper;

    /**
     * 根据 id 获取用户
     * @param id
     * @return
     */ 
    @Override
    @GetMapping("/get/id")
    public User getUserById(Long id) {
        return userService.getById(id);
    }

    /**
     * 根据 id 获取用户列表
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }

    /**
     * 更新用户提交次数
     * @param userId
     * @return
     */
    @Override
    public BaseResponse<Boolean> increaseSubmitCount(Long userId) {
        userService.update()
                .eq("id", userId)
                .gt("submitNumber", 0)
                .setSql("submitNumber = submitNumber + 1")
                .update();
        return new BaseResponse<>(0, true);
    }

    /**
     * 更新用户通过次数
     * @param userId
     * @return
     */
    @Override
    public BaseResponse<Boolean> increaseAcceptCount(Long userId) {
        userService.update()
                .eq("id", userId)
                .gt("acceptNumber", 0)
                .setSql("acceptNumber = acceptNumber + 1")
                .update();
        return new BaseResponse<>(0, true);
    }

    @Override
    public BaseResponse<List<UserSubmitNumber>> getAllUserSubmit() {
        List<UserSubmitNumber> userSubmitList = userService.getUserListBySubmitCount();
        return new BaseResponse<>(0, userSubmitList);
    }
}
