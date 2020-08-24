package com.mine.west.service;

<<<<<<< HEAD
import com.mine.west.models.Account;

import java.util.List;

public interface AccountService {
    /**
     * 根据主键删除账户
     * @param accountID
     * @return
     */
    int deleteByPrimaryKey(Integer accountID);

    /**
     * 新增账户
     * @param record
     * @return
     */
    int insertAccount(Account record);

    /**
     * 根据主键进行更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Account record);

    /**
     * 根据主键找账户
     * @param accountID
     * @return
     */
    Account selectByPrimaryKey(Integer accountID);

    /**
     * 查询所有账户
     * @return
     */
    List<Account> selectAll();

    /**
     * 管理员：通过用户名/邮箱（模糊）查询账户
     *
     * @param name 用户名/邮箱
     * @return 用户对象信息
     */
    public List<Account> selectAccountLikeName(String name);

    /**
     * 通过用户名查询账户
     *
     * @param name 用户名
     * @return 用户对象信息
     */
    public Account selectAccountByName(String name);


    /**
     * 通过邮箱查询账户
     *
     * @param mailbox 邮箱
     * @return 用户对象信息
     */
    public Account selectAccountByMailbox(String mailbox);

    /**
     * 校验用户名称是否唯一
     *
     * @param name 登录名称
     * @return 结果
     */
    public String checkNameUnique(String name);


    /**
     * 校验 mailbox 是否唯一
     *
     * @param record 账户
     * @return 结果
     */
    public String checkMailboxUnique(Account record);
=======

import com.mine.west.exception.AccountException;
import com.mine.west.models.Account;

import java.io.File;

public interface AccountService {

    /**
     * 查询信息（去除密码）
     *
     * @param accountID
     * @return
     * @throws AccountException
     */
    Account getAccount(Integer accountID) throws AccountException;

    /**
     * 查询头像
     *
     * @param headPath
     * @param accountID
     * @return
     * @throws AccountException
     */
    byte[] getAvatar(String headPath, Integer accountID) throws AccountException;

    /**
     * 修改用户信息
     *
     * @param account
     * @return
     * @throws AccountException
     */
    boolean updateAccount(Account account) throws AccountException;

    /**
     * 修改头像
     *
     * @param avatar
     * @param accountID
     * @return
     * @throws AccountException
     */
    boolean updateAvatar(File avatar, Integer accountID) throws AccountException;

>>>>>>> 16335e4c16308fbe28b2fcb6a50239ef43436357
}
