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

    /**
     * 添加action
     *
     * @param action
     * @return
     */
    @PostMapping("/add")
    public Response add(@RequestBody @Validated({SaveActionGroup.class}) Action action) {
        return actionService.add(action);
    }

    /**
     * 删除action
     *
     * @param actionId
     * @return
     */
    @DeleteMapping("/{actionId}")
    public Response delete(@PathVariable Integer actionId) {
        return actionService.delete(actionId);
    }

    /**
     * 更新action
     */
    @PostMapping("/update")
    public Response update(@RequestBody @Validated({SaveActionGroup.class, UpdateGroup.class}) Action action) {
        return actionService.update(action);
    }

    /**
     * 查询action
     *
     * @param action
     * @param pageRequest
     * @return
     */
    @PostMapping("/list")
    public Response list(Action action, PageRequest pageRequest) {
        return actionService.list(action, pageRequest);
    }

    /**
     * action步骤选择action，可供选择的action
     *
     * @param projectId
     * @return
     */
    @GetMapping("/selectable/project/{projectId}/platform/{platform}")
    public Response getSelectableActions(@PathVariable Integer projectId, @PathVariable Integer platform) {
        return actionService.getSelectableActions(projectId, platform);
    }

    /**
     * 调试action
     *
     * @param actionDebugRequest
     * @return
     */
    @PostMapping("/debug")
    public Response debug(@Valid @RequestBody ActionDebugRequest actionDebugRequest) {
        return actionService.debug(actionDebugRequest);
    }

}
