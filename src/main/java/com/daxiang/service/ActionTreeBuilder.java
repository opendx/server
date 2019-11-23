package com.daxiang.service;

import com.daxiang.mbg.mapper.ActionMapper;
import com.daxiang.mbg.po.Action;
import com.daxiang.model.action.Step;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by jiangyitao.
 */
public class ActionTreeBuilder {

    private final ActionMapper actionMapper;

    /**
     * actionId : action
     */
    private final Map<Integer, Action> cachedActions = new HashMap<>();

    public ActionTreeBuilder(ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
    }

    /**
     * 构建action树
     *
     * @return
     */
    public void build(List<Action> actions) {
        for (Action action : actions) {
            List<Step> steps = action.getSteps();
            if (!CollectionUtils.isEmpty(steps)) {
                steps = steps.stream().filter(step -> step.getStatus() == Step.ENABLE_STATUS).collect(Collectors.toList());
                action.setSteps(steps);
                for (Step step : steps) {
                    Action stepAction = cachedActions.get(step.getActionId());
                    if (stepAction == null) {
                        stepAction = actionMapper.selectByPrimaryKey(step.getActionId());
                        cachedActions.put(stepAction.getId(), stepAction);
                    }
                    step.setAction(stepAction);
                    build(Arrays.asList(stepAction));
                }
            }
        }
    }
}
