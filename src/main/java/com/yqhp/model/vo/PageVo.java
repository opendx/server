package com.yqhp.model.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * Created by jiangyitao.
 */
@Data
public class PageVo {
    private Long total;
    private Integer pages;
    private Object data;

    public static PageVo convert(PageInfo pageInfo){
        PageVo pageVo = new PageVo();
        pageVo.setPages(pageInfo.getPages());
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setData(pageInfo.getList());
        return pageVo;
    }
}
