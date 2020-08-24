package com.mine.west.exception.account;

/**
 * 用户密码不正确或不符合规范异常类
 * 
 * @author ruoyi
 */
public class UsernameORmailNotMatchException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UsernameORmailNotMatchException()
    {
        super("用户名不在指定范围内", null);
    }
}
