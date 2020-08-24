package com.mine.west.exception.account;

/**
 * 验证码错误异常类
 * 
 * @author ruoyi
 */
public class CaptchaException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaException()
    {
        super("验证码不正确", null);
    }
}
