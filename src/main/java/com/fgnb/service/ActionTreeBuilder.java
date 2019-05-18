package com.fgnb.service;

import com.fgnb.mbg.mapper.ActionMapper;
import com.fgnb.mbg.po.Action;
import com.fgnb.model.action.Step;
import org.springframework.util.CollectionUtils;

import java.util.*;


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
