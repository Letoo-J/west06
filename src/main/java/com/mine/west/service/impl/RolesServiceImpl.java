package com.mine.west.service.impl;

import com.mine.west.dao.RolesMapper;
import com.mine.west.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesMapper rolesMapper;

    @Override
    public int selectIDByRoleName(String roleName) {
        return rolesMapper.selectIDByRoleName(roleName);
    }
}
