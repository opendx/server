package com.daxiang.utils;

import com.daxiang.App;
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
     * @return baseUrl 如：http://192.168.8.8:8887/static/
     */
    public static String getStaticResourcesBaseUrl() {
        HttpServletRequest request = getCurrentHttpServletRequest();
        String staticPath = App.getProperty("static-path");
        return new StringBuilder()
                .append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(staticPath)
                .append("/")
                .toString();
    }
}
