package com.mine.west.dao;

import com.mine.west.models.RolePers;
import java.util.List;

public interface RolePersMapper {
    int deleteByPrimaryKey(Integer rpID);

    int insert(RolePers record);

    RolePers selectByPrimaryKey(Integer rpID);

    List<RolePers> selectAll();

    int updateByPrimaryKey(RolePers record);
}