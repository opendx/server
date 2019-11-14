package com.daxiang.model.vo;

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
public class ActionCascaderVo extends Action {
    private List<ActionCascaderVo> children;

    public static ActionCascaderVo convert(Action action) {
        ActionCascaderVo actionCascaderVo = new ActionCascaderVo();
        BeanUtils.copyProperties(action, actionCascaderVo);
        return actionCascaderVo;
    }
}
