package com.daxiang.model.environment;

import com.daxiang.validator.group.GlobalVarGroup;
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
    @NotBlank(message = "environmentValue不能为空", groups = {GlobalVarGroup.class})
    private String value;
}
