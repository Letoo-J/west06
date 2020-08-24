package com.mine.west.dao;

import com.mine.west.models.Followlist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface FollowlistMapper {
    int deleteByPrimaryKey(Integer fID);

    int insert(Followlist record);

    Followlist selectByPrimaryKey(Integer fID);

    List<Followlist> selectAll();

    int updateByPrimaryKey(Followlist record);

    List<Followlist> readFollowAccount(Integer accountID);

    List<Followlist> readFanAccount(Integer accountID);

    Followlist readOne(@Param("followID") Integer followID, @Param("fanID") Integer fanID);
}