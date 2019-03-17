package com.yqhp.controller;

import com.yqhp.mbg.po.User;
import com.yqhp.model.Response;
import com.yqhp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录或注册
     * @param user
     * @return
     */
    @PostMapping("/loginOrRegister")
    public Response loginOrRegister(@Valid @RequestBody User user) {
        return userService.loginOrRegister(user);
    }

}
