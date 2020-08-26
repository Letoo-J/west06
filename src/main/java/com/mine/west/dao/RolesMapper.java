package com.mine.west.dao;

import com.mine.west.models.Roles;
import java.util.List;

public interface RolesMapper {
    int deleteByPrimaryKey(Integer roleID);

    int insert(Roles record);

    Roles selectByPrimaryKey(Integer roleID);

    List<Roles> selectAll();

    int updateByPrimaryKey(Roles record);
}