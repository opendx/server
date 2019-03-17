package com.yqhp.model;

import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class PageRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderBy;
}
