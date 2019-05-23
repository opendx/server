package com.fgnb.controller;

import com.fgnb.mbg.po.Action;
import com.fgnb.model.PageRequest;
import com.fgnb.model.Response;
import com.fgnb.model.request.ActionDebugRequest;
import com.fgnb.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Response add(@RequestBody @Valid Action action) {
        return actionService.add(action);
    }

    /**
     * 删除action
     *
     * @param actionId
     * @return
     */
    @GetMapping("/delete/{actionId}")
    public Response delete(@PathVariable Integer actionId) {
        return actionService.delete(actionId);
    }

    /**
     * 更新action
     */
    @PostMapping("/update")
    public Response update(@RequestBody @Valid Action action) {
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
    @GetMapping("/selectable/{projectId}/{platform}")
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
