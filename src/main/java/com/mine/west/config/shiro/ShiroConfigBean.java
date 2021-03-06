package com.mine.west.config.shiro;

import com.mine.west.config.AddressMapping;
import com.mine.west.config.shiro.manager.SessionManager;
import com.mine.west.filter.shiro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  Shiro配置类
 */
@Slf4j
@Configuration
public class ShiroConfigBean {
    /** 踢出后到的地址 */
    @Value("${shiro.user.kickoutUrl}") // ='/login'
    private String kickoutUrl;

    /**  踢出之前登录的/之后登录的用户 默认踢出之前登录的用户 */
    @Value("${shiro.session.kickoutAfter}")  // = false
    private boolean kickoutAfter;

    /**  同一个帐号最大会话数 默认1 */
    @Value("${shiro.session.maxSession}")
    private int maxSession;

    @Value("${spring.redis.host}")
    private String redis_host;

    @Value("${spring.redis.port}")
    private int redis_port;

    @Value("${spring.redis.password}")
    private String redis_password;

    //1.创建shiroFilter(负责拦截所有请求)
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        log.info("------ShiroConfiguration.shirFilter()-------");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 给filter设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 设置login URL
        shiroFilterFactoryBean.setLoginUrl("/account/unauth");
        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.setSuccessUrl("/home");
        // 未授权的页面
        //shiroFilterFactoryBean.setUnauthorizedUrl("/account/unauth");

        // 配置访问权限 必须是LinkedHashMap，因为它必须保证有序
        //<!-- authc:所有url都必须认证通过才可以访问;  anon:所有url都都可以匿名访问-->
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // 对静态资源设置匿名访问
        filterChainDefinitionMap.put("/favicon.ico**", "anon");
        filterChainDefinitionMap.put("/ajax/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/icon/**", "anon");
        filterChainDefinitionMap.put("/image/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/defaultPicture/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/picture/**", "anon");
        filterChainDefinitionMap.put(AddressMapping.resourceHandler, "anon");
        filterChainDefinitionMap.put("/weiboLogin.html", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/scTest.html", "anon");
        // 设置登录的URL为匿名访问，因为一开始没有用户验证
        filterChainDefinitionMap.put("/", "anon,corsFilter");
        filterChainDefinitionMap.put("/account/login", "anon,corsFilter");
        filterChainDefinitionMap.put("/account/register/**", "anon,corsFilter");
        filterChainDefinitionMap.put("/account/unauth", "anon,corsFilter");
        //忘记密码：
        filterChainDefinitionMap.put("/account/find/**", "anon,corsFilter");
        //邮箱验证码
        filterChainDefinitionMap.put("/account/send/emaliVerification", "anon,corsFilter");
        //获得滑块验证码
        filterChainDefinitionMap.put("/verification", "anon,corsFilter");
        //放行第三方登录：
        filterChainDefinitionMap.put("/sina/**", "anon,corsFilter");

        //被shiro拦截的swagger资源放行
        filterChainDefinitionMap.put("/doc.html/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/ui/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/security/**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/configuration/security", "anon");
        filterChainDefinitionMap.put("/configuration/ui", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**/**", "anon");

        // 退出系统的过滤器（记住我状态下，可清除记住我的cookie）
        filterChainDefinitionMap.put("/account/logout", "anon,corsFilter");
        //filterChainDefinitionMap.put("/home/**", "authc");

        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        //其他资源都需要认证  authc 表示需要认证才能进行访问; user表示配置'记住我'或'认证通过'可以访问的地址
        filterChainDefinitionMap.put("/**","kickout,user,corsFilter" );//"kickout,user"
        //filterChainDefinitionMap.put("/**", "anon");//"kickout,user"//"authc"
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //配置自定义拦截器
        Map<String, Filter> filtersMap  = new LinkedHashMap<String, Filter>();
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter());
        //解决session丢失
        filtersMap .put("addPrincipal", addPrincipalToSessionFilter());
        //配置自定义登出 覆盖 logout 之前默认的LogoutFilter
        //filtersMap.put("logout", shiroLogoutFilter());
        //自定义过滤器，前后分离重定向会出现302等ajax跨域错误，这里直接返回错误不重定向
        filtersMap.put("authc", new MyFormAuthenticationFilter());
        //
        filtersMap.put("corsFilter", new RestFilter());
        shiroFilterFactoryBean.setFilters(filtersMap );

        return shiroFilterFactoryBean;
    }

    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入自定义的realm;
        securityManager.setRealm(myShiroRealm());
        //配置记住我
        securityManager.setRememberMeManager(rememberMeManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        //配置自定义session管理，使用redis
        securityManager.setSessionManager(sessionManager());
        // 注入缓存管理器【暂无】;
        //securityManager.setCacheManager(ehCacheManager());

        return securityManager;
    }

    //3.创建自定义realm
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        //设置hashed凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置md5加密
        credentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1023);
        myShiroRealm.setCredentialsMatcher(credentialsMatcher);


        //开启缓存管理器（本地缓存，应用内部）
        //开启全局缓存
        //myShiroRealm.setCachingEnabled(true);

//        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
//        myShiroRealm.setAuthenticationCachingEnabled(true);
//        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
//        myShiroRealm.setAuthorizationCachingEnabled(true);
//        //缓存AuthenticationInfo信息的w缓存名称 在ehcache-shiro.xml中有对应缓存的配置
//        myShiroRealm.setAuthenticationCacheName("authenticationCache");
//        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
//        myShiroRealm.setAuthorizationCacheName("authorizationCache");

        //myShiroRealm.setCacheManager(new EhCacheManager());

        return myShiroRealm;
    }

    /**
     * Shiro自定义过滤器（解决session丢失）
     * @return
     */
    @Bean
    public OncePerRequestFilter addPrincipalToSessionFilter() {
        return new AddPrincipalToSessionFilter();
    }

    /**
     * 	配置Shiro生命周期处理器
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /*
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码; )
     */
//	@Bean
//	public HashedCredentialsMatcher hashedCredentialsMatcher() {
//		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
//		hashedCredentialsMatcher.setHashIterations(1024);// 散列的次数，比如散列两次，相当于md5(md5(""));
//		return hashedCredentialsMatcher;
//	}




    /**
     * shiro缓存管理器;
     * 使用的是shiro-redis开源插件
     * 需要添加到securityManager中
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("accountID");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }



    /*
     * 开启shiro aop注解支持 使用代理方式;所以需要开启代码支持;
     * 同：在pom文件中加入
     *      <dependency>
     *          <groupId>org.springframework.boot</groupId>
     *          <artifactId>spring-boot-starter-aop</artifactId>
     *      </dependency>
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     *	 配置shiro redisManager
     * 	使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redis_host);  //
        redisManager.setPort(redis_port);  //redis_port
        redisManager.setPassword(redis_password);  //
        //redisManager.setExpire(1800);// 配置缓存过期时间
        redisManager.setTimeout(0);

        return redisManager;
    }

    /**
     * Session Manager
     * 	使用的是shiro-redis开源插件
     * 	DefaultWebSessionManager
     */
    @Bean
    public SessionManager sessionManager() {
        SessionManager sessionManager = new SessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);

        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(cacheManager());

        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(1800000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);

        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(3600000);
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }

    /**
     * 配置session监听
     * @return
     */
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * 默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("Token");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * shiro sessionDao层的实现 通过redis ：使用的是shiro-redis开源插件
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public RedisSessionDAO sessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        //session在redis中的保存时间,最好大于session会话超时时间
        redisSessionDAO.setExpire(12000);
        return redisSessionDAO;
    }


    /**
     * 	限制同一账号登录同时登录人数控制
     *
     * @return
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());

        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
        kickoutSessionControlFilter.setKickoutAfter(kickoutAfter);  //

        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(maxSession);  //
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl(kickoutUrl);  //kickoutUrl

        return kickoutSessionControlFilter;
    }


    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     *  DefaultAdvisorAutoProxyCreator实现了BeanProcessor接口,
     * 	当ApplicationContext读如所有的Bean配置信息后，这个类将扫描上下文,
     * 	找出所有的Advistor(一个切入点和一个通知的组成),将这些Advisor应用到所有符合切入点的Bean中
     *  @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
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

    /**
     * 配置LogoutFilter
     * @return
     */
    public ShiroLogoutFilter shiroLogoutFilter(){
        ShiroLogoutFilter shiroLogoutFilter = new ShiroLogoutFilter();
        //配置登出后重定向的地址，等出后配置跳转到登录接口
        shiroLogoutFilter.setRedirectUrl("/account/logout");
        return shiroLogoutFilter;
    }

    /**
     * 解决： 无权限页面不跳转 shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized") 无效
     * shiro的源代码ShiroFilterFactoryBean.Java定义的filter必须满足filter instanceof AuthorizationFilter，
     * 只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，
     * 所以unauthorizedUrl设置后页面不跳转 Shiro注解模式下，登录失败与没有权限都是通过抛出异常。
     * 并且默认并没有去处理或者捕获这些异常。在SpringMVC下需要配置捕获相应异常来通知用户信息
     * @return
     */
    /*@Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver simpleMappingExceptionResolver=new SimpleMappingExceptionResolver();
        Properties properties=new Properties();
        //这里的 /unauthorized 是页面，不是访问的路径
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException","/unauthorized");
        properties.setProperty("org.apache.shiro.authz.UnauthenticatedException","/unauthorized");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        return simpleMappingExceptionResolver;
    }*/

}


