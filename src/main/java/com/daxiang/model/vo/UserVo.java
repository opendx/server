package com.daxiang.model.vo;

import com.daxiang.mbg.po.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class UserVo extends User {
    private String token;

    public static UserVo convert(User user, String token) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setToken(token);
        return userVo;
    }
}
