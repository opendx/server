package com.daxiang.service;

import com.daxiang.mbg.po.Action;
import com.daxiang.model.action.LocalVar;
import com.daxiang.model.action.Step;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by jiangyitao.
 */
public class ActionProcessor {

    private final ActionService actionService;
    private final EnvironmentService environmentService;
    private final Integer env;

    /**
     * actionId : action
     */
    private final Map<Integer, Action> cachedActions = new HashMap<>();

    public ActionProcessor(ActionService actionService, EnvironmentService environmentService, Integer env) {
        this.actionService = actionService;
        this.environmentService = environmentService;
        this.env = env;
    }

    public void process(List<Action> actions) {
        actions.forEach(action -> {
            handleActionLocalVarsValue(action);
            cachedActions.put(action.getId(), action);
        });
        recursivelyProcess(actions);
    }

    private void recursivelyProcess(List<Action> actions) {
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
                    recursivelyProcess(Arrays.asList(stepAction));
                }
            }

            // actionImports
            List<Integer> actionImports = action.getActionImports();
            if (!CollectionUtils.isEmpty(actionImports)) {
                List<Action> importActions = actionImports.stream()
                        .map(this::getActionById)
                        .collect(Collectors.toList());
                action.setImportActions(importActions);
                recursivelyProcess(importActions);
            }
        }
    }

    private Action getActionById(Integer actionId) {
        Action action = cachedActions.get(actionId);
        if (action == null) {
            action = actionService.getActionById(actionId);
            handleActionLocalVarsValue(action);
            cachedActions.put(actionId, action);
        }
        return action;
    }

    private void handleActionLocalVarsValue(Action action) {
        List<LocalVar> localVars = action.getLocalVars();
        if (!CollectionUtils.isEmpty(localVars)) {
            localVars.forEach(localVar -> {
                String value = environmentService.getValueInEnvironmentValues(localVar.getEnvironmentValues(), env);
                localVar.setValue(value);
            });
        }
    }
}
