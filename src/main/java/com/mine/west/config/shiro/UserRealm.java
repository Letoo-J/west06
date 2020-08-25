package com.mine.west.config.shiro;

import com.mine.west.exception.account.*;
import com.mine.west.models.Account;
import com.mine.west.service.impl.LoginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义realm实现类,用于实现具体的验证和授权方法
 * @author Bean
 *
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private LoginServiceImpl _loginService;

    //自定义token：
    @Override
    public boolean supports(AuthenticationToken token){
        return token != null && token instanceof AccountToken;
    }


    /**
     *  认证
     * 	参数：AuthenticationToken是从表单穿过来封装好的对象
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("认证-doGetAuthenticationInfo():" + token);

        // 将AuthenticationToken强转为AuthenticationToken对象
        //这边是界面的登陆数据，将数据封装成token (从loginController传来的)
        AccountToken upToken = (AccountToken) token;

        // 获得从表单传过来的用户名
        String username = upToken.getUsername();
        String password = "";
        String uString;
        if (upToken.getPassword() != null) {
            password = new String(upToken.getPassword()); //拿到表单的密码
        }

        Account account = null;
        try
        {
            //尝试登入！（里面进行 验证码、密码加盐校验）
            account = _loginService.login(username, password);
            log.info("登录成功！");
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

        // 创建SimpleAuthenticationInfo对象，并且把account和password等信息封装到里面
        // 用户密码的比对是Shiro帮我们完成的
        // getName()：当前realm对象的名称，调用分类的getName()
        //传入Account信息,返回数据库查询到的信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(account, account.getPassword(),
                                                ByteSource.Util.bytes(account.getSalt()), getName());
        return info;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        return null;
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
}
