package com.mine.west.service.impl;

import com.mine.west.constant.AccountConstants;
import com.mine.west.dao.AccountMapper;
import com.mine.west.dao.PersMapper;
import com.mine.west.models.Account;
import com.mine.west.models.Pers;
import com.mine.west.service.AccountService;
import com.mine.west.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PersMapper persMapper;

    @Override
    public int deleteByPrimaryKey(Integer accountID) {
        return accountMapper.deleteByPrimaryKey(accountID);
    }

    @Override
    public int insertAccount(Account record) {
        return accountMapper.insertAccount(record);
    }

    @Override
    public int updateByPrimaryKey(Account record) {
        return accountMapper.updateByPrimaryKey(record);
    }

    @Override
    public Account selectByPrimaryKey(Integer accountID) {
        return accountMapper.selectByPrimaryKey(accountID);
    }

    @Override
    public List<Account> selectAll() {
        return accountMapper.selectAll();
    }

    @Override
    public List<Account> selectAccountLikeName(String name) {
        return accountMapper.selectAccountLikeName(name);
    }

    @Override
    public Account selectAccountByName(String name) {
        return accountMapper.selectAccountByName(name);
    }

    @Override
    public Account selectAccountByMailbox(String mailbox) {
        return accountMapper.selectAccountByMailbox(mailbox);
    }

    @Override
    public String checkNameUnique(String name) {
        int count = accountMapper.checkNameUnique(name);
        if (count > 0)
        {
            return AccountConstants.USER_NAME_NOT_UNIQUE; //1
        }
        return AccountConstants.USER_NAME_UNIQUE; //0
    }

    @Override
    public String checkMailboxUnique(Account record) {
        Integer accountId = (int) (StringUtils.isNull(record.getAccountID()) ? -1 : record.getAccountID());
        Account info = accountMapper.checkMailboxUnique(record.getMailbox());
        if (StringUtils.isNotNull(info) && info.getAccountID() != accountId )
        {
            return AccountConstants.USER_EMAIL_NOT_UNIQUE;  //1
        }
        return AccountConstants.USER_EMAIL_UNIQUE;  //0
    }

    @Override
    public Account findRolesByName(String username) {
        return accountMapper.findRolesByName(username);
    }

    @Override
    public List<Pers> findPermsByRoleId(Integer id) {
        return persMapper.findPermsByRoleId(id);
    }

}
