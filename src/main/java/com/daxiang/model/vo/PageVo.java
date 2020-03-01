package com.daxiang.model.vo;

import com.daxiang.mbg.po.Page;
import com.daxiang.utils.HttpServletUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class PageVo extends Page {
    private String creatorNickName = "";
    private String imgUrl;

    public String getImgUrl() {
        if (!StringUtils.isEmpty(getImgPath()))
            return HttpServletUtil.getStaticResourceUrl(getImgPath());
        return null;
    }
}
