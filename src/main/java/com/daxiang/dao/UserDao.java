package com.daxiang.dao;

import com.daxiang.model.dto.UserDto;
import org.apache.ibatis.annotations.Param;

/**
 * Created by jiangyitao.
 */
public interface UserDao {
    UserDto selectUserDtoByUsername(@Param("username") String username);
}
