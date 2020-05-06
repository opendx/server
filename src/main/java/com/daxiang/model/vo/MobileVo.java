package com.daxiang.model.vo;

import com.daxiang.mbg.po.Mobile;
import com.daxiang.utils.HttpServletUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class MobileVo extends Mobile {
    private String imgUrl;

    public String getImgUrl() {
        if (!StringUtils.isEmpty(getImgPath()))
            return HttpServletUtil.getStaticResourceUrl(getImgPath());
        return null;
    }
}
