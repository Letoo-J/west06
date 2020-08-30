package com.mine.west.config.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.west.exception.account.*;
import com.mine.west.exception.base.BaseException;
import com.mine.west.models.Account;
import com.mine.west.models.Pers;
import com.mine.west.service.AccountService;
import com.mine.west.service.impl.LoginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 自定义realm实现类,用于实现具体的验证和授权方法
 * @author Bean
 *
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private LoginServiceImpl _loginService;

    @Autowired
    private AccountService _accountService;

    //自定义token：
    @Override
    public boolean supports(AuthenticationToken token){
        return token != null && token instanceof AccountToken;
    }

    /**
     *  授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("---------------- 执行 Shiro 权限获取 ---------------------");

        //获取身份信息
        Account primaryPrincipal = (Account) principals.getPrimaryPrincipal();
        log.info("doGetAuthorizationInfo()调用授权验证: "+primaryPrincipal);
        //根据主身份信息(用户名)获取角色 和 权限信息
        Account account = _accountService.findRolesByName(primaryPrincipal.getName());

        //授权角色信息
        if(!CollectionUtils.isEmpty(account.getRoles())){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            account.getRoles().forEach(role->{
                simpleAuthorizationInfo.addRole(role.getRoleName());
                //权限信息(当前role)
                List<Pers> perms1 = _accountService.findPermsByRoleId(role.getRoleID());
                HashSet<Pers> perms2 = new HashSet<Pers>(perms1);
                if(!CollectionUtils.isEmpty(perms2)){
                    perms2.forEach(perm->{
                        simpleAuthorizationInfo.addStringPermission(perm.getPersName());
                    });
                }
            });
            log.info("---------------- 获取到以下角色 ----------------");
            log.info(simpleAuthorizationInfo.getRoles().toString());
            log.info("---------------- 获取到以下权限 ----------------");
            log.info(simpleAuthorizationInfo.getStringPermissions().toString());
            log.info("---------------- Shiro 权限获取成功 ----------------------");
            return simpleAuthorizationInfo;
        }
        return null;
    }

    /**
     *  认证
     *  参数：AuthenticationToken是从表单穿过来封装好的对象
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("---------------- 执行 Shiro 凭证认证 ----------------------");
        log.info("认证-doGetAuthenticationInfo():" + token);

        // 将AuthenticationToken强转为AuthenticationToken对象
        //这边是界面的登陆数据，将数据封装成token (从loginController传来的)
        AccountToken upToken = (AccountToken) token;

        // 获得从表单传过来的用户名
        String username = upToken.getUsername();
        String password = "";

        String t_password = upToken.getPassword();
        Account account = null;
        //若为第三方登录，则不自动设置密码
        if(t_password == null){
            log.info("----------【第三方】- Shiro 凭证认证 ------------");
            try{
                account = _loginService.TPlogin(username);
            }catch (BaseException e){
                throw new AuthenticationException(e.getMessage(), e);
            }
            //this.deleteSession(username);

            log.info("---------------- 【第三方】- Shiro 凭证认证成功 ----------------------");
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(account, account.getPassword(),
                    ByteSource.Util.bytes(account.getSalt()), getName());
            return info;
        }

        password = new String(t_password); //拿到表单的密码
        try {
            //尝试登入！（里面进行 验证码、密码加盐校验）
            account = _loginService.login(username, password);
            log.info("【登录成功的用户】："+ account);
            //uString = JSON.toJSONString(user);
        }
        catch (CaptchaException e) {
            throw new AuthenticationException(e.getMessage(), e);
        }
        catch (UserNotExistsException e) {
            throw new UnknownAccountException(e.getMessage(), e);
        }
        catch (UserPasswordNotMatchException e) {
            throw new IncorrectCredentialsException(e.getMessage(), e);
        }
        catch (UserPasswordRetryLimitExceedException e) {
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        }
        catch (UserBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (RoleBlockedException e) {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (Exception e) {
            log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
            throw new AuthenticationException(e.getMessage(), e);
        }
        //this.deleteSession(username);

        log.info("---------------- Shiro 凭证认证成功 ----------------------");
        // 创建SimpleAuthenticationInfo对象，并且把account和password等信息封装到里面
        // 用户密码的比对是Shiro帮我们完成的
        // getName()：当前realm对象的名称，调用分类的getName()
        //传入Account信息,返回数据库查询到的信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(account, account.getPassword(),
                ByteSource.Util.bytes(account.getSalt()), getName());
        return info;
    }

    private void deleteSession(String username){
        //处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        //获取当前已登录的用户session列表
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        if(sessions.size()>0){
            for(Session session:sessions){
                //清除该用户以前登录时保存的session
                if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)!=null){
                    Object obj = ((SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)).asList().get(0);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Account tUser = objectMapper.convertValue(obj, Account.class);
                    if(username.equals(tUser.getName())) {
                        log.info("认证时，清除该用户以前登录时保存的session");
                        sessionManager.getSessionDAO().delete(session);
                    }
                }
            }
        }
    }

    /**
     * 建议重写此方法，提供唯一的缓存Key!!!!【才可以进行正常退出、清除缓存】
     */
    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        Account account = (Account) principals.getPrimaryPrincipal();
        return account.getAccountID();
//      	return super.getAuthorizationCacheKey(principals);
    }


    /**
     * 建议重写此方法，提供唯一的缓存Key
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
        Account account = (Account) principals.getPrimaryPrincipal();
        return account.getAccountID();
//        return super.getAuthenticationCacheKey(principals);
    }


    /**
     * 重写方法,清除当前用户的的 授权缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     * @param principals
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
