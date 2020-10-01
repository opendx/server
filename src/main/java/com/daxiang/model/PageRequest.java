package com.daxiang.model;

import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class PageRequest {

    private Integer pageNum;
    private Integer pageSize;

    /**
     * 是否要分页
     *
     * @return
     */
    public boolean needPaging() {
        return (pageNum != null && pageNum > 0) && (pageSize != null && pageSize > 0);
    }
}
