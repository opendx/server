package com.daxiang.controller;

import com.daxiang.mbg.po.User;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.dto.UserDto;
import com.daxiang.security.SecurityUtil;
import com.daxiang.service.UserService;
import com.daxiang.validator.group.SaveUserGroup;
import com.daxiang.validator.group.UpdateGroup;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        userService.add(userDto);
        return Response.success("添加成功");
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{userId}")
    public Response delete(@PathVariable Integer userId) {
        userService.delete(userId);
        return Response.success("删除成功");
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/update")
    public Response update(@Validated({SaveUserGroup.class, UpdateGroup.class}) @RequestBody UserDto userDto) {
        userService.update(userDto);
        return Response.success("更新成功");
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/list")
    public Response list(User query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<UserDto> pagedData = userService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<UserDto> userDtos = userService.getUserDtos(query, orderBy);
            return Response.success(userDtos);
        }
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody User user) {
        String token = userService.login(user);
        return Response.success("登陆成功", ImmutableMap.of("token", token));
    }

    @GetMapping("/info")
    public Response getInfo() {
        return Response.success(SecurityUtil.getCurrentUserDto());
    }

    @PostMapping("/logout")
    public Response logout() {
        return Response.success();
    }

}
