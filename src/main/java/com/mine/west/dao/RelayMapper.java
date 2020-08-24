package com.mine.west.dao;

import com.mine.west.models.Relay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RelayMapper {
    int deleteByPrimaryKey(Integer rID);

    int insert(Relay record);

    Relay selectByPrimaryKey(Integer rID);

    List<Relay> selectAll();

    int updateByPrimaryKey(Relay record);
}