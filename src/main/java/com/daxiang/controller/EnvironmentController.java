package com.daxiang.controller;

import com.daxiang.mbg.po.Environment;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.service.EnvironmentService;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/environment")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @PostMapping("/add")
    public Response add(@Valid @RequestBody Environment environment) {
        return environmentService.add(environment);
    }

    @DeleteMapping("/{environmentId}")
    public Response delete(@PathVariable Integer environmentId) {
        return environmentService.delete(environmentId);
    }

    @PostMapping("/update")
    public Response update(@Validated({UpdateGroup.class}) @RequestBody Environment environment) {
        return environmentService.update(environment);
    }

    @PostMapping("/list")
    public Response list(Environment environment, PageRequest pageRequest) {
        return environmentService.list(environment, pageRequest);
    }
}
