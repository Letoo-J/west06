package com.mine.west.service.impl;

import com.mine.west.constant.AccountConstants;
import com.mine.west.dao.AccountMapper;
import com.mine.west.exception.account.*;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.util.ServletUtils;
import com.mine.west.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl {

    @Autowired
    private AccountMapper _accountMapper;

    /**
     * @param name
     * @param password
     * @return  Account
     */
    public Account login(String name, String password)
    {

        // 用户名/邮箱或密码为空 错误
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(password)){
            //return Result.of(02, "用户名或密码为空");
            throw new UserNotExistsException();
        }

        // 密码如果不在指定范围内 错误
        if (password.length() < AccountConstants.PASSWORD_MIN_LENGTH
                || password.length() > AccountConstants.PASSWORD_MAX_LENGTH){
            //return Result.of(03, "密码不在指定范围内");
            throw new UsernameORpasswordNotExistsException();
        }

        // 用户名/邮箱不在指定范围内 错误
        /*if (name.length() < AccountConstants.USERNAME_MIN_LENGTH){
            //return Result.of(05, "用户名不在指定范围内");
            throw new UsernameORmailNotMatchException();
        }*/

        /********************************************************************/
        // 查询用户信息
        Account account = null ;
        account = _accountMapper.selectAccountByName(name);
        if (account == null )   //若用户名找不到，则根据邮箱查找&& maybeEmail(username)
        {
            account = _accountMapper.selectAccountByMailbox(name);
        }

        //用户不存在
        if (account == null)
        {
            //return Result.of(06, "用户不存在");
            throw new UserNotExistsException();  //抛出异常
        }

        //校验用户是否被禁用
        if (!account.getIdentity().equals("0"))
        {
            //return Result.of(8, "用户已被封禁！");
            throw new UserBlockedException();
        }

        return account;
    }

    /**
     * 第三方登录
     * @param idstr 第三方登录的注册账户
     * @return
     */
    public Account TPlogin(String idstr){
        Account account = _accountMapper.selectAccountByName(idstr);

        //用户不存在
        if (account == null) {
            throw new UserNotExistsException();  //抛出异常：第三方用户不存在
        }

        //校验用户是否被禁用
        if (!account.getIdentity().equals("0")) {
            throw new UserBlockedException();  //用户已被封禁！
        }

        return account;
    }
}
