package com.fgnb.model;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Data
public class Page<T> {
    private Long total;
    private List<T> data;

    public static long getTotal(List data) {
        return new PageInfo(data).getTotal();
    }

    public static <T> Page<T> build(List<T> data, long total) {
        Page page = new Page();
        page.setTotal(total);
        page.setData(data);
        return page;
    }
}

