package com.daxiang.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiangyitao.
 */
public class HttpServletUtil {

    public static HttpServletRequest getCurrentHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 获取静态资源下载的baseUrl
     *
     * @return baseUrl 如：http://192.168.8.8:8887/
     */
    public static StringBuilder getStaticResourceBaseUrl() {
        HttpServletRequest request = getCurrentHttpServletRequest();
        return new StringBuilder()
                .append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append("/");
    }

    public static String getStaticResourceUrl(String filePath) {
        return getStaticResourceBaseUrl().append(filePath).toString();
    }
}
