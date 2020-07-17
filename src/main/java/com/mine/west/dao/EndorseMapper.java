package com.mine.west.dao;

import com.mine.west.models.Endorse;
import java.util.List;

public interface EndorseMapper {
    int deleteByPrimaryKey(Integer eID);

    int insert(Endorse record);

    Endorse selectByPrimaryKey(Integer eID);

    List<Endorse> selectAll();

    int updateByPrimaryKey(Endorse record);
}