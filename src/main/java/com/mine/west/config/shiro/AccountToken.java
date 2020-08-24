package com.mine.west.config.shiro;

//AccountToken
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class AccountToken implements AuthenticationToken, RememberMeAuthenticationToken{

    private static final long serialVersionUID = 1L;

    /**
     * The verifyInput 验证码
     */
    //private String verifyInput;//新增的校验因子

    /**
     * The username
     */
    private String username;

    /**
     * The password
     */
    private String password;

    /**
     * The rememberMe 记住我选项
     */
    private boolean rememberMe = false;

    //verifyInput
    /*public String getVerifyInput() {
        return verifyInput;
    }
    public void setVerifyInput(String verifyInput) {
        this.verifyInput = verifyInput;
    }*/

    //username
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    //password
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    //remeberMe
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    public boolean getRememberMe() {
        return rememberMe;
    }

    public Object getPrincipal() {
        return getUsername();
    }
    public Object getCredentials() {
        return getPassword();
    }


    public AccountToken() {}
    public AccountToken(final String username, final String password,
                    final boolean rememberMe) {

        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe ;
    }


    @Override
    public boolean isRememberMe() {
        // TODO Auto-generated method stub
        return rememberMe;
    }



}

