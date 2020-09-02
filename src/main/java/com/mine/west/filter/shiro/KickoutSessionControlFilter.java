package com.mine.west.filter.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.mine.west.models.Account;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSON;

public class KickoutSessionControlFilter extends AccessControlFilter {

    /** 踢出后到的地址 */
    private String kickoutUrl;

    /**  踢出之前登录的/之后登录的用户 默认踢出之前登录的用户 */
    private boolean kickoutAfter = false;

    /**  同一个帐号最大会话数 默认1 */
    private int maxSession = 1;

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache");
    }

    /**
     * 	表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
     * (感觉这里应该是对白名单（不需要登录的接口）放行的)
     * 	如果isAccessAllowed返回true则onAccessDenied方法不会继续执行
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }


    /**
     * 	表示当访问拒绝时是否已经处理了；如果返回true表示自己不处理且继续拦截器链执行；
     * 	如果返回false表示该拦截器实例已经处理了，将直接返回即可 /（比如重定向到另一个页面）
     *
     *  onAccessDenied是否执行取决于isAccessAllowed的值，
     * 	如果返回true则onAccessDenied不会执行；如果返回false，执行onAccessDenied
     * 	如果onAccessDenied也返回false，则直接返回，
     * 	不会进入请求的方法（只有isAccessAllowed和onAccessDenied的情况下）
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.err.println(">>>>>>>>>>>>>>>>Session 队列>>>>>>>>>>>>>>>>>>");

        Subject subject = getSubject(request, response);
        System.out.println("subject.getPrincipal():" + subject.getPrincipal());


        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程s
            System.out.println("没有登录，直接进行之后的流程");
            return true;
        }

        System.out.println("登录了，进行缓存");
        Session session = subject.getSession();
        /*这里获取的User是实体。因为在 自定义ShiroRealm中的doGetAuthenticationInfo方法中
          new SimpleAuthenticationInfo(user, password, getName());
                          传的是 User实体 所以这里拿到的也是实体,如果传的是userName 这里拿到的就是userName*/

        //JSONObject jsonObject = JSONObject.parseObject((String)subject.getPrincipal());
        //String uID = jsonObject.getString("UID");

        Account user = (Account) subject.getPrincipal();
        //System.out.println("getUID()："+user.getUID());
        String uID = user.getAccountID().toString();

        Serializable sessionId = session.getId();

        //读取缓存,没有就存入. 初始化用户的队列放到缓存里
        Deque<Serializable> deque = cache.get(uID);

        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
        //就new一个空队列，不然deque对象为空，会报空指针
        if(deque==null){
            deque = new LinkedList<Serializable>();
            //cache.put(uID, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            //将sessionId存入队列
            deque.push(sessionId);
            //将用户的sessionId队列缓存
            cache.put(uID, deque);
        }

        //若改密码了，踢出去原有的？

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
                //踢出后再更新下缓存队列
                cache.put(uID, deque);
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();

                //踢出后再更新下缓存队列
                cache.put(uID, deque);
            }

            try {
                //获取被踢出的sessionId的session对象
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));

                if(kickoutSession != null) {
                    System.out.println("tttttttttttttttttt");
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            }
            catch (Exception e) {//ignore exception
                e.printStackTrace();
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                //退出登录
                subject.logout();
            }
            catch (Exception e) { //ignore
                e.printStackTrace();
            }
            //保存访问路径
            saveRequest(request);


            Map<String, String> resultMap = new HashMap<String, String>();
            //判断是不是Ajax请求
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
                resultMap.put("user_status", "300");
                resultMap.put("message", "您已经在其他地方登录，请重新登录！");
                //输出json串
                out(response, resultMap);
            }else{
                //重定向
                WebUtils.issueRedirect(request, response, kickoutUrl);
            }
            /*
            HttpServletRequest httpRequest = WebUtils.toHttp(request);
            if (com.frame.utils.WebUtils.isAjax(httpRequest)) {
            	logger.debug("当前用户已经在其他地方登录，并且是Ajax请求！");
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                // 向http头添加 状态 sessionstatus
                httpServletResponse.setHeader("sessionstatus", "kickout");
                // 向http头添加登录的url
                httpServletResponse.addHeader("loginPath", kickoutUrl);
//                httpServletResponse.sendError(ShiroFilterUtils.HTTP_STATUS_SESSION_EXPIRE);
                return false;
            } else {
            	request.setAttribute("user_status", "300");
            	request.setAttribute("message", "您已经在其他地方登录，请重新登录！");
            	request.getRequestDispatcher(kickoutUrl).forward(request, response);
                return false;
            }*/

            return false;
        }
        return true;
    }


    private void out(ServletResponse hresponse, Map<String, String> resultMap)
            throws IOException {
        try {
            hresponse.setCharacterEncoding("UTF-8");
            PrintWriter out = hresponse.getWriter();
            out.println(JSON.toJSONString(resultMap));
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("KickoutSessionFilter.class 输出JSON异常，可以忽略。");
        }
    }
}
