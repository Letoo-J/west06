package com.mine.west.service.impl;

import com.mine.west.dao.AccountRoleMapper;
import com.mine.west.models.AccountRole;
import com.mine.west.service.AccountRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountRoleServiceImpl implements AccountRoleService {
    @Autowired
    private AccountRoleMapper accountRoleMapper;

    /**
     * 修改用户的角色
     * @param ar
     * @return
     */
    @Override
    public int updateAccoutRole(AccountRole ar) {
        return accountRoleMapper.updateByAccountID(ar);
    }
}
