package com.mine.west.dao;

import com.mine.west.models.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectMapper {
    int insert(Collect collect);

    List<Collect> selectByAccountID(Integer accountID);

    int getBlogNumber(Integer blogID);

    Collect readOne(@Param("accountID") Integer accountID, @Param("blogID") Integer blogID);
}
