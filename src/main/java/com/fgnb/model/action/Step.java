package com.fgnb.model.action;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fgnb.mbg.po.Action;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jiangyitao.
 * action步骤
 */
@Data
public class Step {
    /**
     * 调用的action id
     */
    @NotNull(message = "步骤的actionId不能为空")
    private Integer actionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Action action;
    /**
     * 步骤名
     */
    @NotBlank(message = "步骤名不能为空")
    private String name;
    /**
     * 步骤赋值
     */
    private String evaluation;
    /**
     * 步骤号，即第几步
     */
    @NotNull(message = "步骤号不能为空")
    private Integer number;
    /**
     * 调用action传入的值
     */
    private List<ParamValue> paramValues;

}
