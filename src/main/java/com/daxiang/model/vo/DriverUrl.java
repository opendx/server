package com.daxiang.model.vo;

import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class DriverUrl {
    /** 1.windows 2.linux 3.macos */
    private Integer platform;
    private String downloadUrl;
}
