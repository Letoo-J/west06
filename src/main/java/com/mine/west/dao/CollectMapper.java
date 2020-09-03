package com.mine.west.dao;

import com.mine.west.models.Collect;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectMapper {
    int insert(Collect collect);

    List<Collect> selectByAccountID(Integer accountID);
}
