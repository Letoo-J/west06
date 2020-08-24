package com.mine.west.dao;

import com.mine.west.models.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BlogMapper {
    int deleteByPrimaryKey(Integer blogID);

    int insert(Blog record);

    Blog selectByPrimaryKey(Integer blogID);

    List<Blog> selectAll();

    int updateByPrimaryKey(Blog record);

    List<Blog> selectByAccountID(Integer accountID);
}