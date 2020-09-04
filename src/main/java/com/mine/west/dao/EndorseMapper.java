package com.mine.west.dao;

import com.mine.west.models.Endorse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface EndorseMapper {
    int deleteByPrimaryKey(Integer eID);

    int insert(Endorse record);

    Endorse selectByPrimaryKey(Integer eID);

    List<Endorse> selectAll();

    int updateByPrimaryKey(Endorse record);

    List<Endorse> selectByAccountID(Integer accountID);

    Endorse readOne(@Param("accountID") Integer accountID, @Param("blogID") Integer blogID);
}