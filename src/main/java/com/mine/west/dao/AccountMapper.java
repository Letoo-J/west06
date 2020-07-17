package com.mine.west.dao;

import com.mine.west.models.Account;
import java.util.List;

public interface AccountMapper {
    int deleteByPrimaryKey(Integer accountID);

    int insert(Account record);

    Account selectByPrimaryKey(Integer accountID);

    List<Account> selectAll();

    int updateByPrimaryKey(Account record);
}