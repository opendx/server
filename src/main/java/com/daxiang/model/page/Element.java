package com.daxiang.model.page;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Element {
    @NotEmpty(message = "element name不能为空")
    private String name;
    @NotEmpty(message = "findBy不能为空")
    private String findBy;
    @NotEmpty(message = "value不能为空")
    private String value;
}
