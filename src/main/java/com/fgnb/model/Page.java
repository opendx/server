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
    private Integer pages;
    private List<T> data;

    public static <T> Page<T> convert(List<T> data){
        PageInfo pageInfo = new PageInfo(data);

        Page page = new Page();
        page.setPages(pageInfo.getPages());
        page.setTotal(pageInfo.getTotal());
        page.setData(pageInfo.getList());

        return page;
    }
}
