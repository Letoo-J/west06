package com.mine.west.dao;

import com.mine.west.models.Accountoperation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountoperationMapper {
    int deleteByPrimaryKey(Integer aID);

    int insert(Accountoperation record);

    Accountoperation selectByPrimaryKey(Integer aID);

    Accountoperation select(@Param("accountID") Integer accountID, @Param("blogID") Integer blogID);

    List<Accountoperation> selectAll();

    int updateByPrimaryKey(Accountoperation record);
}