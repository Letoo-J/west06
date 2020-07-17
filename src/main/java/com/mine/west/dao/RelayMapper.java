package com.mine.west.dao;

import com.mine.west.models.Relay;
import java.util.List;

public interface RelayMapper {
    int deleteByPrimaryKey(Integer rID);

    int insert(Relay record);

    Relay selectByPrimaryKey(Integer rID);

    List<Relay> selectAll();

    int updateByPrimaryKey(Relay record);
}