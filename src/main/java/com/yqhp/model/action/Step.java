package com.yqhp.model.action;

import lombok.Data;

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
    private Integer actionId;
    /**
     * 步骤名
     */
    private String name;
    /**
     * 步骤赋值
     */
    private String evaluation;
    /**
     * 步骤号，即第几步
     */
    private Integer number;
    /**
     * 调用action传入的值
     */
    private List<ParamValue> paramValues;

}
