package com.daxiang.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.daxiang.utils.HttpServletUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Created by jiangyitao.
 */
@Data
public class DriverFile {
    /**
     * 1.windows 2.linux 3.macos
     */
    private Integer platform;
    private String filePath;

    @JSONField(serialize = false) // 不会序列化的表中
    private String downloadUrl;

    public String getDownloadUrl() {
        if (!StringUtils.isEmpty(filePath)) {
            return HttpServletUtil.getStaticResourceUrl(filePath);
        }
        return null;
    }
}
