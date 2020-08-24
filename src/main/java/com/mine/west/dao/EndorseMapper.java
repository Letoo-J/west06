package com.mine.west.dao;

import com.mine.west.models.Endorse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EndorseMapper {
    int deleteByPrimaryKey(Integer eID);

    int insert(Endorse record);

    Endorse selectByPrimaryKey(Integer eID);

    List<Endorse> selectAll();

    int updateByPrimaryKey(Endorse record);
}