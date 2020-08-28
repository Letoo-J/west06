package com.mine.west.filter.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hui.yunfei@qq.com on 2019/11/18
 */
@Slf4j
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    public MyFormAuthenticationFilter() {
        super();
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        log.info("进入自定义shiro拦截器isAccessAllowed方法");
        if(request instanceof HttpServletRequest){
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")){
                log.info("进入自定义shiro拦截器isAccessAllowed方法:OPTIONS请求");
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        log.info("进入身份认证失败filter");
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setStatus(200);
//        httpServletResponse.setContentType("application/json;charset=utf-8");
//        PrintWriter pw = httpServletResponse.getWriter();
//        ResultObj result=new ResultObj();
//        result.setInfo(401);
//        result.setMsg("身份认证失败，请重新登录");
//        pw.write(JSONObject.toJSONString(result));
//        pw.flush();
//        pw.close();
//        return false;
        WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
