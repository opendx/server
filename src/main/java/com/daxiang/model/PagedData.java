package com.daxiang.model;

import lombok.Data;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Data
public class PagedData<T> {
    private long total;
    private List<T> data;

    public PagedData(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }
}

