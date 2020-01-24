package com.daxiang.service;

import com.daxiang.mbg.mapper.ActionMapper;
import com.daxiang.mbg.po.Action;
import com.daxiang.model.action.Step;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by jiangyitao.
 */
public class ActionTreeBuilder {

    private final List<Action> actions;
    private final ActionMapper actionMapper;

    /**
     * actionId : action
     */
    private final Map<Integer, Action> cachedActions = new HashMap<>();

    public ActionTreeBuilder(List<Action> actions, ActionMapper actionMapper) {
        Assert.notNull(actions, "actions cannot be null");
        Assert.notNull(actionMapper, "actionMapper cannot be null");

        this.actions = actions;
        this.actionMapper = actionMapper;
    }

    public void build() {
        actions.forEach(action -> cachedActions.put(action.getId(), action));
        build(this.actions);
    }

    private void build(List<Action> actions) {
        for (Action action : actions) {
            // steps
            List<Step> steps = action.getSteps();
            if (!CollectionUtils.isEmpty(steps)) {
                steps = steps.stream()
                        .filter(step -> step.getStatus() == Step.ENABLE_STATUS) // 过滤掉未开启的步骤
                        .collect(Collectors.toList());
                action.setSteps(steps);
                for (Step step : steps) {
                    Action stepAction = getActionById(step.getActionId());
                    step.setAction(stepAction);
                    build(Arrays.asList(stepAction));
                }
            }

            // actionImports
            List<Integer> actionImports = action.getActionImports();
            if (!CollectionUtils.isEmpty(actionImports)) {
                List<Action> importActions = actionImports.stream()
                        .map(actionId -> getActionById(actionId))
                        .collect(Collectors.toList());
                action.setImportActions(importActions);
                build(importActions);
            }
        }
    }

    private Action getActionById(Integer actionId) {
        Action action = cachedActions.get(actionId);
        if (action == null) {
            action = actionMapper.selectByPrimaryKey(actionId);
            cachedActions.put(actionId, action);
        }
        return action;
    }
}
