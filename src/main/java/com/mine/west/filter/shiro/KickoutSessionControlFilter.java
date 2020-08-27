package com.mine.west.filter.shiro;

import com.alibaba.fastjson.JSON;
import com.mine.west.constant.ResultStatusCode;
import com.mine.west.models.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
public class KickoutSessionControlFilter extends AccessControlFilter {
    private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

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
        log.info(">>>>>>>>>>>>>>>>Session 队列>>>>>>>>>>>>>>>>>>");
        Subject subject = getSubject(request, response);
        log.info("subject.getPrincipal():" + subject.getPrincipal());
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            log.info("没有登录，直接进行之后的流程");
            return true;
        }

        log.info("登录了，进行缓存");
        Session session = subject.getSession();
         /*这里获取的Account是实体。因为在 自定义ShiroRealm中的doGetAuthenticationInfo方法中
          new SimpleAuthenticationInfo(Account, password,salt, getName());
          传的是 Account实体 所以这里拿到的也是实体,如果传的是userName 这里拿到的就是userName*/
        Account account = (Account) subject.getPrincipal();
        String username = account.getName();
        Serializable sessionId = session.getId();

        //读取缓存   没有就存入
        Deque<Serializable> deque = cache.get(username);

        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
        //就new一个空队列，不然deque对象为空，会报空指针
        if(deque == null){
            deque = new LinkedList<Serializable>();
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            //将sessionId存入队列
            deque.push(sessionId);
            //将用户的sessionId队列缓存
            cache.put(username, deque);
        }

        //若改密码了，踢出去原有的？

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
                log.info("踢出后者");
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
                log.info("踢出前者");
                //踢出后再更新下缓存队列
                cache.put(username, deque);
            }

            try {
                //获取被踢出的sessionId的session对象
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {//ignore exception

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
                resultMap.put("code", ResultStatusCode.INVALID_TOKEN.toString());
                resultMap.put("msg", "您已经被踢出");
                //输出json串
                out(response, resultMap);
            }else{
                //重定向
                WebUtils.issueRedirect(request, response, kickoutUrl);
            }
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

    public String getKickoutUrl() {
        return kickoutUrl;
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public boolean isKickoutAfter() {
        return kickoutAfter;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public int getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Cache<String, Deque<Serializable>> getCache() {
        return cache;
    }

    //设置Cache的key的前缀
    public void setCache(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache");
    }
}
