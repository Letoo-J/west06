package com.mine.west.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mine.west.constant.AccountConstants;
import com.mine.west.dao.AccountMapper;
import com.mine.west.email.MailboxVerificationUtil;
import com.mine.west.exception.AccountException;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.util.SaltUtils;
import com.mine.west.util.ServletUtils;
import com.mine.west.util.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl {

    @Autowired
    private AccountMapper _accountMapper;   //用户校验

//	@Autowired
//    private PasswordService passwordService;  //密码校验(注册不需要)

    public String register(String username, String password,
                           String mail, String verifyInput) {

        Map<String, Object> map = new HashMap();

        boolean b = false;
        // 验证码校验
        try {
            b = MailboxVerificationUtil.verificationCodeIsLegal(verifyInput,mail);
        } catch (AccountException e) {
            e.printStackTrace();
        }
        if(!b){  //校验不成功
            return "验证码不正确";
        }
        //删除此条注册验证码的map记录

        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return "用户名或密码为空";
        }

        // 密码如果不在指定范围内 错误
        if (password.length() < AccountConstants.PASSWORD_MIN_LENGTH
                || password.length() > AccountConstants.PASSWORD_MAX_LENGTH){
            return "密码不在指定范围内";
        }

        // 用户名不在指定范围内 错误
        if (username.length() < AccountConstants.USERNAME_MIN_LENGTH
                || username.length() > AccountConstants.USERNAME_MAX_LENGTH){
            return "用户名不在指定范围内";
        }

        // 查询用户信息(也可以直接查询结果条数--更好)
        Account account1 = _accountMapper.selectAccountByName(username);
        Account account2 = _accountMapper.selectAccountByMailbox(mail);
        if (account1 != null) {  //若用户名已经存在;
            return "用户名已经存在";
        }
        if (account2 != null) {  //若邮箱已经存在
            return "邮箱已经存在";
        }

        //验证邮箱是否符合格式
        if(!maybeEmail(mail)) {
            return "邮箱不符合格式";
        }

        //验证密码是否符合格式
        if(!rightPassword(password)) {
            return "密码不符合格式";
        }


        Account account = new Account();
        //1.生成随机盐：
        String salt = SaltUtils.getSalt(10);
        //2.明文密码进行md5 + salt + hash散列
        Md5Hash md5Hash = new Md5Hash(password, salt,1023);
        //3设置盐值:
        account.setSalt(salt);
        account.setName(username);
        account.setPassword(md5Hash.toHex());
        account.setMailbox(mail);

        if(_accountMapper.insertAccount(account) == 0) {  //插入失败
            return "注册失败....有一股神秘力量.....";
        }
        return "注册成功";
    }

    /**
     * 第三方登录时，先注册
     * @param idstr 字符串型的用户UID
     * @return Account
     */
    public Account TPregister(String idstr){
        Account account01 = _accountMapper.selectAccountByName(idstr);
        //若之前已经第三方登录过，则不需要注册
        if(account01 != null){
            return account01;
        }

        Account account = new Account();
        account.setName(idstr);
        _accountMapper.insertAccount(account);
        //获得默认密码：
        account = _accountMapper.selectAccountByName(idstr);
        //1.生成随机盐：
        String salt = SaltUtils.getSalt(10);
        String d_password = new Md5Hash(account.getPassword(), salt,1023).toHex();
        account.setSalt(salt);
        account.setPassword(d_password);
        //更新用户
        _accountMapper.updateByPrimaryKey(account);

        return account;
    }

    private boolean maybeEmail(String mail)
    {
        if (!mail.matches(AccountConstants.EMAIL_PATTERN))
        {
            return false;
        }
        return true;
    }

    private boolean rightPassword(String password)
    {
        if (!password.matches(AccountConstants.PASSWORD_PATTERN))
        {
            return false;
        }
        return true;
    }
}

