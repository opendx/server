package com.daxiang.model.page;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by jiangyitao.
 */
@Data
public class Element {
    @NotBlank(message = "element name不能为空")
    private String name;
    @NotBlank(message = "findBy不能为空")
    private String findBy;
    @NotBlank(message = "value不能为空")
    private String value;
}
