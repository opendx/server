package com.daxiang.model.action;

import com.daxiang.validator.annotation.JavaIdentifier;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by jiangyitao.
 * action参数
 */
@Data
public class Param {
    /**
     * 参数类型
     */
    @NotBlank(message = "方法参数类型不能为空")
    private String type;
    /**
     * 参数名
     */
    @JavaIdentifier(message = "方法参数名不合法")
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

