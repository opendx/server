package com.fgnb.controller;

import com.fgnb.mbg.po.User;
import com.fgnb.model.Response;
import com.fgnb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Response login(@Valid @RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/register")
    public Response register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public Response getInfo() {
        return userService.getInfo();
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public Response logout() {
        return userService.logout();
    }

}
