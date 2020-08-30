package com.mine.west.service.impl;

import com.mine.west.dao.AccountMapper;
import com.mine.west.dao.FollowlistMapper;
import com.mine.west.exception.AccountException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.exception.FollowlistException;
import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.Followlist;
import com.mine.west.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    FollowlistMapper followlistMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public int readFollowNumber(Integer accountID) throws ModelException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        return (followlistMapper.readFollowAccount(accountID)).size();
    }

    @Override
    public int readFanNumber(Integer accountID) throws ModelException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        return (followlistMapper.readFanAccount(accountID)).size();
    }

    @Override
    public List<Followlist> readFollowAccount(Integer accountID) throws ModelException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        return followlistMapper.readFollowAccount(accountID);
    }

    @Override
    public List<Followlist> readFanAccount(Integer accountID) throws ModelException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        return followlistMapper.readFanAccount(accountID);
    }

    @Override
    public int create(Followlist followlist) throws ModelException {
        if (followlistMapper.readOne(followlist.getFollowID(), followlist.getFanID()) != null)
            throw new FollowlistException(ExceptionInfo.FOLLOW_EXIT);
        Account account1 = accountMapper.selectByPrimaryKey(followlist.getFanID());
        Account account2 = accountMapper.selectByPrimaryKey(followlist.getFollowID());
        if (account1 == null || account2 == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        followlistMapper.insert(followlist);
        return followlist.getfID();
    }

    @Override
    public boolean cancelFollow(Followlist followlist) throws ModelException {
        Followlist f = followlistMapper.readOne(followlist.getFollowID(), followlist.getFanID());
        if (f == null)
            throw new FollowlistException(ExceptionInfo.FOLLOW_NOT_EXIT);
        followlistMapper.deleteByPrimaryKey(f.getfID());
        return true;
    }
}
