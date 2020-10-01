package com.daxiang.controller;

import com.daxiang.mbg.po.Action;
import com.daxiang.model.PageRequest;
import com.daxiang.model.PagedData;
import com.daxiang.model.Response;
import com.daxiang.model.dto.ActionTreeNode;
import com.daxiang.model.request.ActionDebugRequest;
import com.daxiang.model.vo.ActionVo;
import com.daxiang.service.ActionService;
import com.daxiang.validator.group.SaveActionGroup;
import com.daxiang.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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
        actionService.add(action);
        return Response.success("添加成功");
    }

    @PostMapping("/resetBasicAction")
    public Response resetBasicAction(@RequestBody List<Action> actions) {
        actionService.resetBasicAction(actions);
        return Response.success();
    }

    @DeleteMapping("/{actionId}")
    public Response delete(@PathVariable Integer actionId) {
        actionService.delete(actionId);
        return Response.success("删除成功");
    }

    @PostMapping("/update")
    public Response update(@RequestBody @Validated({SaveActionGroup.class, UpdateGroup.class}) Action action) {
        actionService.update(action);
        return Response.success("更新成功");
    }

    @PostMapping("/list")
    public Response list(Action query, String orderBy, PageRequest pageRequest) {
        if (pageRequest.needPaging()) {
            PagedData<ActionVo> pagedData = actionService.list(query, orderBy, pageRequest);
            return Response.success(pagedData);
        } else {
            List<ActionVo> actionVos = actionService.getActionVos(query, orderBy);
            return Response.success(actionVos);
        }
    }

    @GetMapping("/cascader")
    public Response cascader(Integer projectId, Integer platform, Integer type) {
        List<ActionTreeNode> tree = actionService.cascader(projectId, platform, type);
        return Response.success(tree);
    }

    @PostMapping("/debug")
    public Response debug(@Valid @RequestBody ActionDebugRequest actionDebugRequest) {
        return actionService.debug(actionDebugRequest);
    }

}
