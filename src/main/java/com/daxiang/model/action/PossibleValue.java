package com.daxiang.model.action;

import lombok.Data;

/**
 * Created by jiangyitao.
 * action参数可能值
 */
@Data
public class PossibleValue {
    /**
     * 可能值
     */
    private String value;
    /**
     * 可能值描述
     */
    private String description;
}
