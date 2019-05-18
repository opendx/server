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

    public static <T> Page<T> convert(List<T> data){
        PageInfo pageInfo = new PageInfo(data);
        Page pageVo = new Page();
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setData(pageInfo.getList());
        return pageVo;
    }
}

