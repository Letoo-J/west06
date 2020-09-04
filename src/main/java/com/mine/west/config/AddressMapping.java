package com.mine.west.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 地址映射
 */
@Configuration
public class AddressMapping implements WebMvcConfigurer {
    public static final String resourceHandler = "/pictureImage/**";
    public static final String location = "/root/myhome/src/main/resources/picture/";
//    public static final String location="D://vc_image/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler(resourceHandler).addResourceLocations("file:///" + location);
    }
}
