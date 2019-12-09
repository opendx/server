package com.daxiang.model.vo;

import com.daxiang.mbg.po.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by jiangyitao.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageCascaderVo extends Page {

    private List<PageCascaderVo> children;

    public static PageCascaderVo convert(Page page) {
        PageCascaderVo pageCascaderVo = new PageCascaderVo();
        BeanUtils.copyProperties(page, pageCascaderVo);
        return pageCascaderVo;
    }
}
