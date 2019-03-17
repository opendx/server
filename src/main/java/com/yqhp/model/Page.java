package com.yqhp.model;

import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class Page {
    private Long total;
    private Integer pages;
    private Object data;

    public static Page convert(PageInfo pageInfo){
        Page pageVo = new Page();
        pageVo.setPages(pageInfo.getPages());
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setData(pageInfo.getList());
        return pageVo;
    }
}
