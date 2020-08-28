package com.mine.west.filter.csrf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import javax.servlet.http.HttpSession;

import com.mine.west.models.Account;
import com.mine.west.util.AjaxResponseWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  CSRF跨域请求伪造拦截
 *	 除登录以外的post方法，都需要携带token，如果token为空或token错误，则返回异常提示
 * 	注意在filter初始化参数内配置排除的url
 * @author Lenovo
 *
 */
@Slf4j
public class CsrfFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(CsrfFilter.class);

    public List<String> excludes = new ArrayList<String>();

    private static boolean isOpen = false;  //是否开启该filter

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,ServletException {
        if(!isOpen){
            filterChain.doFilter(request, response);
            return ;
        }
        if(logger.isDebugEnabled()){
            logger.debug("csrf filter is running");
            System.err.println("csrf filter is running");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        //得到登入时放入session的token
        String token = (String) session.getAttribute("uuidToken");
        Account u = (Account) session.getAttribute("account");
        log.info("uuidToken:" + token);
        log.info("User-crsf:"+ u);


        if(!"POST".equalsIgnoreCase(req.getMethod()) || handleExcludeURL(req, resp) || token == null ){
            if(token == null) {
                // 产生新的 token 放入 session 中
                token = UUID.randomUUID().toString();
                session.setAttribute("uuidToken",token );
                System.out.println("更新token:"+ token);
                //AjaxResponseWriter.println(resp, "更新token，请重试。。。");
            }
            filterChain.doFilter(request, response);
            return;
        }

        //得到前端传来的token
        // 从 HTTP头 中取得 uuidToken
        String requestToken1 = req.getHeader("uuidToken"); //getHeader
        // 从 请求参数 中取得 uuidToken
        String requestToken2 = req.getParameter("uuidToken");
        String requestToken = null;
        if( requestToken1 != null ){
            requestToken = requestToken1;
        }else if( requestToken2 != null ){
            requestToken = requestToken2;
        }
        System.out.println("requestToken:"+ requestToken);

        //进行比对
        if(StringUtils.isBlank(requestToken) || !requestToken.equals(token)){
            AjaxResponseWriter.write(req, resp, "这是csrf攻击！！！");
            return;
        }
        filterChain.doFilter(request, response);
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
            logger.debug("csrf filter init~~~~~~~~~~~~");
        }

        String temp = filterConfig.getInitParameter("excludes");
        if (temp != null) {
            String[] url = temp.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }

        temp = filterConfig.getInitParameter("isOpen");
        if(StringUtils.isNotBlank(temp) && "true".equals(temp)){
            isOpen = true;
        }
    }

    @Override
    public void destroy() {}



}


