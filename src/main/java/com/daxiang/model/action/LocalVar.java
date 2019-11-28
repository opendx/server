package com.daxiang.model.action;

import com.daxiang.model.environment.EnvironmentValue;
import com.daxiang.validator.annotation.NoDuplicateEnvironment;
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
    @NotBlank(message = "局部变量名不能为空")
    private String name;
    /**
     * 局部变量值
     */
    @Valid
    @NoDuplicateEnvironment(message = "局部变量环境不能重复")
    private List<EnvironmentValue> environmentValues;
    /**
     * 变量描述
     */
    private String description;
}
