package com.mine.west.filter.XSS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拦截防止xss注入
 * 通过Jsoup过滤请求参数内的特定字符
 * @author yangwk
 */
public class XssFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(XssFilter.class);

    private static boolean isOpen = false; //是否过滤富文本内容

    public List<String> excludes = new ArrayList<String>();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("打开 xss filter");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if(handleExcludeURL(req, resp)){
            filterChain.doFilter(request, response);
            return;
        }

        /*
         * 	通过XssHttpServletRequestWrapper将HttpServletRequest进行了封装
         *
         * 	保证了后续代码执行request.getParameter，request.getParameterValues，request.getHeader等
         * 	时调用的都是XssHttpServletRequestWrapper内重写的方法，
         * 	获取到的参数是已经进行过标签过滤的内容，从而消除了敏感信息。
         */
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request,isOpen);
        filterChain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {

        if (excludes == null || excludes.isEmpty()) {
            return false;
        }

        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("初始化xss filter");
        }

        String temp = filterConfig.getInitParameter("excludes");
        if (temp != null) {
            String[] url = temp.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }

        temp = filterConfig.getInitParameter("isOpen");
        if(StringUtils.isNotBlank(temp)){
            isOpen = BooleanUtils.toBoolean(temp);
        }

    }

    @Override
    public void destroy() {}

}

