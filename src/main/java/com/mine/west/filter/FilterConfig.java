package com.mine.west.filter;

import java.util.Map;

import com.mine.west.filter.XSS.XssFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

@Configuration
public class FilterConfig {

    @Value("${csrf.filter.enabled}")
    private String isCsrfFilterOpen;

    @Value("${csrf.filter.excludes}")
    private String crdfExcludes;

    @Value("${xss.filter.enabled}")
    private String isIncludeRichText;

    @Value("${xss.filter.excludes}")
    private String xssExcludes;

    @Value("${shiro.session.timeout}")
    private String serverSessionTimeout;

    /**
     * csrf过滤拦截器
     */
    /*@Bean
    public FilterRegistrationBean<CsrfFilter> csrfFilterRegistrationBean() {
        //通过FilterRegistrationBean实例设置优先级可以生效
        //通过@WebFilter无效
        FilterRegistrationBean<CsrfFilter> filterRegistrationBean = new FilterRegistrationBean<CsrfFilter>();
        //注册自定义过滤器
        filterRegistrationBean.setFilter(new CsrfFilter());
        filterRegistrationBean.setName("csrfFilter"); //过滤器名称
        filterRegistrationBean.addUrlPatterns("/*");  //过滤所有路径
        filterRegistrationBean.setOrder(2);  //优先级(数字越大,优先级越低)
        filterRegistrationBean.setEnabled(true);

        Map<String, String> initParameters = Maps.newHashMap();
        initParameters.put("excludes", crdfExcludes);   //登入注册不进行拦截
        initParameters.put("isOpen", isCsrfFilterOpen);
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }
*/
    /**
     * xss过滤拦截器
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
        //通过FilterRegistrationBean实例设置优先级可以生效
        //通过@WebFilter无效
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<XssFilter>();
        //注册自定义过滤器
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setName("xssFilter"); //过滤器名称
        filterRegistrationBean.addUrlPatterns("/*"); //过滤所有路径
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);

        Map<String, String> initParameters = Maps.newHashMap();
        //配置不需要参数过滤的请求url
        initParameters.put("excludes", xssExcludes);
        //用于设置富文本true
        initParameters.put("isOpen", isIncludeRichText);
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }


//	@Bean
//	public FilterRegistrationBean shiroSessionFilterRegistrationBean() {
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//		filterRegistrationBean.setFilter(new ShiroSessionFilter());
//		filterRegistrationBean.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
//		filterRegistrationBean.setEnabled(true);
//		filterRegistrationBean.addUrlPatterns("/*");
//		Map<String, String> initParameters = Maps.newHashMap();
//		initParameters.put("serverSessionTimeout", serverSessionTimeout);
//		initParameters.put("excludes", "/favicon.ico,/hhh.png,/css/*,/others/*,/js/*,/webjars/*,/img/*,/ajax/*");
//		filterRegistrationBean.setInitParameters(initParameters);
//		return filterRegistrationBean;
//	}


}

