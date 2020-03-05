package com.daxiang.model.action;

import com.daxiang.model.environment.EnvironmentValue;
import com.daxiang.validator.annotation.JavaIdentifier;
import com.daxiang.validator.annotation.NoDuplicateEnvironment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by jiangyitao.
 * action局部变量
 */
@Data
public class LocalVar {
    /**
     * 参数类型
     */
    @NotBlank(message = "局部变量类型不能为空")
    private String type;
    /**
     * 局部变量名
     */
    @JavaIdentifier(message = "局部变量名不合法")
    private String name;
    /**
     * 局部变量值
     */
    @Valid
    @NoDuplicateEnvironment(message = "局部变量环境不能重复")
    private List<EnvironmentValue> environmentValues;

    /**
     * 专门给agent用的
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    /**
     * 变量描述
     */
    private String description;
}
