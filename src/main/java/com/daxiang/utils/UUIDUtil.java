package com.daxiang.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by jiangyitao.
 */
public class UUIDUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String getUUIDFilename(String filename) {
        Assert.hasText(filename, "filename不能为空");
        String fileExtension = FilenameUtils.getExtension(filename);
        String uuid = getUUID();
        return StringUtils.isEmpty(fileExtension) ? uuid : uuid + "." + fileExtension;
    }
}
