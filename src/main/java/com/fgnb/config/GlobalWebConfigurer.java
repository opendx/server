package com.fgnb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by jiangyitao.
 * 解决跨域
 */
@Configuration
public class GlobalWebConfigurer implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").//所有请求，解决跨域问题
                allowedOrigins("*");
    }
}
