package com.mine.west.exception.account;

/**
 * 用户锁定异常类
 * 
 * @author ruoyi
 */
public class UserBlockedException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserBlockedException()
    {
        super("用户已被封禁", null);
    }
}
