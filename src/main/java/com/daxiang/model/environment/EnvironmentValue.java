package com.daxiang.model.environment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by jiangyitao.
 */
@Data
public class EnvironmentValue {

    public static final int DEFAULT_ENVIRONMENT_ID = -1;

    @NotNull(message = "environmentId不能为空")
    private Integer environmentId;
    @NotBlank(message = "环境值不能为空")
    private String value;
}
