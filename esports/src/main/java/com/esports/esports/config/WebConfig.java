package com.esports.esports.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        // 对外暴露 /static/** → 本地文件夹
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
