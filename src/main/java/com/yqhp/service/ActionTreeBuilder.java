package com.yqhp.service;

import com.yqhp.mbg.mapper.ActionMapper;
import com.yqhp.mbg.po.Action;
import com.yqhp.model.action.Step;
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
                    Action cachedAction = cachedActions.get(step.getActionId());
                    if (cachedAction == null) {
                        cachedAction = actionMapper.selectByPrimaryKey(step.getActionId());
                        cachedActions.put(cachedAction.getId(), cachedAction);
                    }
                    step.setAction(cachedAction);
                    build(Arrays.asList(cachedAction));
                }
            }
        }
    }
}
