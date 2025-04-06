package com.Anglyao.foreverojbackendquestionservice.mapper;

import com.Anglyao.foreverojbackendmodel.model.dto.user.UserSubmitNumber;
import com.Anglyao.foreverojbackendmodel.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户数据库操作
 *
 * @author <a href="https://github.com/RicardoLou">RicardoLou</a>
 */
public interface UserSubmitMapper extends BaseMapper<User> {
    @Select("SELECT id, userName, submitNumber FROM user")
    List<UserSubmitNumber> findAllUsersSubmitNumber();
}




