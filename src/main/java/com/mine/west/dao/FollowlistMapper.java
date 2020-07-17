package com.mine.west.dao;

import com.mine.west.models.Followlist;
import java.util.List;

public interface FollowlistMapper {
    int deleteByPrimaryKey(Integer fID);

    int insert(Followlist record);

    Followlist selectByPrimaryKey(Integer fID);

    List<Followlist> selectAll();

    int updateByPrimaryKey(Followlist record);
}