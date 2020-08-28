package com.mine.west.service;

public interface RolesService {
    /**
     * 查询roleName对应的roleID
     * @param roleName
     * @return
     */
    int selectIDByRoleName(String roleName);
}
