package com.daxiang.model.vo;

import com.daxiang.mbg.po.Page;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class PageVo extends Page {
    private String creatorNickName;

    public static PageVo convert(Page page, String creatorNickName) {
        PageVo pageVo = new PageVo();
        BeanUtils.copyProperties(page, pageVo);
        pageVo.setCreatorNickName(creatorNickName);
        return pageVo;
    }
}
