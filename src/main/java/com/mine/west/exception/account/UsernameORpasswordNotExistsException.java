package com.mine.west.exception.account;

/**
 * 用户不存在异常类
 * 
 * @author ruoyi
 */
public class UsernameORpasswordNotExistsException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UsernameORpasswordNotExistsException()
    {
        super("密码不在指定范围内", null);
    }
}
