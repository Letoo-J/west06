/*
package com.mine.west;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.ServletContext;


@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    //  请在properties文件中添加该属性，判断是否开启swagger
    @Value("${swagger.enable}")
    private boolean swaggerEnable;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/");
        // 判断是否启用swagger文档界面,启用则会开放这些资源，让开发者能够访问到
        if (swaggerEnable) {
            registry.addResourceHandler("/doc.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        super.addResourceHandlers(registry);
    }

    @SuppressWarnings({"unchecked"})
    @Bean
    public FilterRegistrationBean normalCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置你要允许的网站域名，如果全允许则设为 *
        config.addAllowedOrigin("*");
        // 如果要限制 HEADER 或 METHOD 请自行更改
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        // 这个顺序很重要哦，为避免麻烦请设置在最前面
        bean.setOrder(0);
        return bean;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setSessionTimeout(12*60*60);
        super.setServletContext(servletContext);
    }
}*/
