package com.mine.west.service;


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

}
