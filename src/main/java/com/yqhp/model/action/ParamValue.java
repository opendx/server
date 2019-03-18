package com.yqhp.model.action;

import lombok.Data;

/**
 * Created by jiangyitao.
 * <p>
 * 步骤传入的参数值
 */
@Data
public class ParamValue {
    /**
     * 参数名
     */
    private String paramName;
    /**
     * 参数值
     */
    private String paramValue;
}
