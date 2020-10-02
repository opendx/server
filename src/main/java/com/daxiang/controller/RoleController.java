package com.daxiang.controller;

import com.daxiang.mbg.po.Role;
import com.daxiang.model.Response;
import com.daxiang.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public Response list() {
        List<Role> roles = roleService.list();
        return Response.success(roles);
    }
}
