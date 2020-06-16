package com.daxiang.model.dto;

import com.daxiang.mbg.po.Action;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionTreeNode extends Action {

    private List<ActionTreeNode> children;

    public static ActionTreeNode create(Action action) {
        ActionTreeNode actionTreeNode = new ActionTreeNode();
        BeanUtils.copyProperties(action, actionTreeNode);
        return actionTreeNode;
    }
}
