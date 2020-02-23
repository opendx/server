package com.daxiang.controller;

import com.daxiang.mbg.po.User;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.dto.UserDto;
import com.daxiang.service.UserService;
import com.daxiang.validator.group.SaveUserGroup;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/add")
    public Response add(@Validated({SaveUserGroup.class}) @RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{userId}")
    public Response delete(@PathVariable Integer userId) {
        return userService.delete(userId);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/update")
    public Response update(@Validated({SaveUserGroup.class, UpdateGroup.class}) @RequestBody UserDto userDto) {
        return userService.update(userDto);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/list")
    public Response list(User user, PageRequest pageRequest) {
        return userService.list(user, pageRequest);
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody User user) {
        return userService.login(user);
    }

    @GetMapping("/info")
    public Response getInfo() {
        return userService.getInfo();
    }

    @PostMapping("/logout")
    public Response logout() {
        return userService.logout();
    }

}
