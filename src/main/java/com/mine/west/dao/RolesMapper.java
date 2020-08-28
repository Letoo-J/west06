package com.mine.west.dao;

import com.mine.west.models.Roles;
import java.util.List;

public interface RolesMapper {

    /**
     * 查询roleName对应的roleID
     * @param roleName
     * @return
     */
    int selectIDByRoleName(String roleName);

    int deleteByPrimaryKey(Integer roleID);

    int insert(Roles record);

    Roles selectByPrimaryKey(Integer roleID);

    List<Roles> selectAll();

    int updateByPrimaryKey(Roles record);
}