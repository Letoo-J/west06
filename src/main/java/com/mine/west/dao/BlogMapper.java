package com.mine.west.dao;

import com.mine.west.models.Blog;
import java.util.List;

public interface BlogMapper {
    int deleteByPrimaryKey(Integer blogID);

    int insert(Blog record);

    Blog selectByPrimaryKey(Integer blogID);

    List<Blog> selectAll();

    int updateByPrimaryKey(Blog record);
}