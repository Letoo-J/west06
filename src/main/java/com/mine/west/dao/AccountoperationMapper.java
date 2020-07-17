package com.mine.west.dao;

import com.mine.west.models.Accountoperation;
import java.util.List;

public interface AccountoperationMapper {
    int deleteByPrimaryKey(Integer aID);

    int insert(Accountoperation record);

    Accountoperation selectByPrimaryKey(Integer aID);

    List<Accountoperation> selectAll();

    int updateByPrimaryKey(Accountoperation record);
}