package com.mine.west.dao;

import com.mine.west.models.AccountRole;
import java.util.List;

public interface AccountRoleMapper {
    int deleteByPrimaryKey(Integer arID);

    int insert(AccountRole record);

    AccountRole selectByPrimaryKey(Integer arID);

    List<AccountRole> selectAll();

    int updateByPrimaryKey(AccountRole record);
}