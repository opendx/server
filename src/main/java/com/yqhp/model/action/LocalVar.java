package com.yqhp.model.action;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by jiangyitao.
 * action局部变量
 */
@Data
public class LocalVar {
    /**
     * 局部变量名
     */
    @NotBlank(message = "actionLocalVarName不能为空")
    private String name;
    /**
     * 局部变量值
     */
    private String value;
    /**
     * 变量描述
     */
    private String description;
}
