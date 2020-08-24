package com.mine.west.config.shiro;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  Shiro配置类
 */
@Slf4j
@Configuration
public class ShiroConfigBean {

    //1.创建shiroFilter(负责拦截所有请求)
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        log.info("------ShiroConfiguration.shirFilter()-------");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 给filter设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 设置login URL
        shiroFilterFactoryBean.setLoginUrl("/account/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/home");
        // 未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        // 配置访问权限 必须是LinkedHashMap，因为它必须保证有序
        //<!-- authc:所有url都必须认证通过才可以访问;  anon:所有url都都可以匿名访问-->
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // 对静态资源设置匿名访问
        // 设置登录的URL为匿名访问，因为一开始没有用户验证
        filterChainDefinitionMap.put("/account/login", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/account/register/**", "anon");
        filterChainDefinitionMap.put("/user/checkCode", "anon");
        //放行验证码：
        filterChainDefinitionMap.put("/getVerifyCode", "anon");
        //获得RES公钥、模块
        filterChainDefinitionMap.put("/getMoudle", "anon");
        //忘记密码：
        filterChainDefinitionMap.put("/resetPassword", "anon");
        filterChainDefinitionMap.put("/user/resetPassword", "anon");
        filterChainDefinitionMap.put("/user/resetPasswordForm", "anon");
        //放行头像
        filterChainDefinitionMap.put("/pic/**", "anon");
        //filterChainDefinitionMap.put("/Exception.class", "anon");

        // 退出系统的过滤器（记住我状态下，可清除记住我的cookie）
        filterChainDefinitionMap.put("/account/logout", "logout");
        //filterChainDefinitionMap.put("/home/**", "authc");

        //**admin的url，要用角色是admix的才可以登录,对应的拦截器是RolesAuthorizationFilte
        //filterChainDefinitionMap.put("/admin", "roles[admin]");

        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        //filterChainDefinitionMap.put("/**", "authc");
        //其他资源都需要认证  authc 表示需要认证才能进行访问; user表示配置'记住我'或'认证通过'可以访问的地址
        filterChainDefinitionMap.put("/**","authc" );//"kickout,user"

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        /*//配置自定义拦截器
        Map<String, Filter> filtersMap  = new LinkedHashMap<String, Filter>();
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter());
        //解决session丢失
        filtersMap .put("addPrincipal", addPrincipalToSessionFilter());
        //配置自定义登出 覆盖 logout 之前默认的LogoutFilter
        filtersMap.put("logout", shiroLogoutFilter());
        shiroFilterFactoryBean.setFilters(filtersMap );*/

        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入自定义的realm;
        securityManager.setRealm(userRealm());
        //配置记住我
        securityManager.setRememberMeManager(rememberMeManager());
        // 自定义缓存实现 使用redis
        //securityManager.setCacheManager(cacheManager());
        //配置自定义session管理，使用redis
        //securityManager.setSessionManager(sessionManager());
        // 注入缓存管理器【暂无】;
        //securityManager.setCacheManager(ehCacheManager());

        return securityManager;
    }

    //3.创建自定义realm
    @Bean
    public UserRealm userRealm() {
        UserRealm myShiroRealm = new UserRealm();
        //设置hashed凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置md5加密
        credentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1023);
        myShiroRealm.setCredentialsMatcher(credentialsMatcher);

        myShiroRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        //myShiroRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        //myShiroRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        //myShiroRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        //myShiroRealm.setAuthorizationCacheName("authorizationCache");
        return myShiroRealm;
    }




    /**
     *	记住我cookie
     * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true); //Cookie 不可通过客户端脚本访问
        simpleCookie.setPath("/");
        //<!-- 记住我cookie生效时间15天 ,单位秒;-->
        simpleCookie.setMaxAge(1296000); //30天---2592000
        return simpleCookie;
    }

    /**
     * 	记住我管理器
     * cookie管理对象;记住我功能,rememberMe管理器
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

    /**
     *	记住我Filter
     * FormAuthenticationFilter 过滤器 过滤记住我
     * @return
     */
    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }


    /*
     * 开启shiro aop注解支持 使用代理方式;所以需要开启代码支持;
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
