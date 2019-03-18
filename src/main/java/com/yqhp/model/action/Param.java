package com.yqhp.model.action;

import lombok.Data;

import java.util.List;

/**
 * Created by jiangyitao.
 * action参数
 */
@Data
public class Param {
    /**
     * 参数名
     */
    private String name;
    /**
     * 参数描述
     */
    private String description;
    /**
     * 可能的值，供调用方参考
     */
    private List<PossibleValue> possibleValues;

}

