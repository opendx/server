package com.daxiang.controller;

import com.daxiang.mbg.po.Action;
import com.daxiang.model.PageRequest;
import com.daxiang.model.Response;
import com.daxiang.model.request.ActionDebugRequest;
import com.daxiang.service.ActionService;
import com.daxiang.validator.group.SaveActionGroup;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by jiangyitao.
 */
@RestController
@RequestMapping("/action")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @PostMapping("/add")
    public Response add(@RequestBody @Validated({SaveActionGroup.class}) Action action) {
        return actionService.add(action);
    }

    @DeleteMapping("/{actionId}")
    public Response delete(@PathVariable Integer actionId) {
        return actionService.delete(actionId);
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({SaveActionGroup.class, UpdateGroup.class}) Action action) {
        return actionService.update(action);
    }

    @PostMapping("/list")
    public Response list(Action action, PageRequest pageRequest) {
        return actionService.list(action, pageRequest);
    }

    @GetMapping("/cascader")
    public Response cascader(Integer projectId, Integer platform, Integer type) {
        return actionService.cascader(projectId, platform, type);
    }

    @PostMapping("/debug")
    public Response debug(@Valid @RequestBody ActionDebugRequest actionDebugRequest) {
        return actionService.debug(actionDebugRequest);
    }

}
