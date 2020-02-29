package com.daxiang.model.vo;

import com.daxiang.mbg.po.Device;
import com.daxiang.utils.HttpServletUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class DeviceVo extends Device {
    private String imgUrl;

    public String getImgUrl() {
        if (!StringUtils.isEmpty(getImgName()))
            return HttpServletUtil.getStaticResourcesBaseUrl() + getImgName();
        return null;
    }
}
