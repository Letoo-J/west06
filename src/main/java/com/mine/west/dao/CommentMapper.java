package com.mine.west.dao;

import com.mine.west.models.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CommentMapper {
    int deleteByPrimaryKey(Integer commentID);

    int insert(Comment record);

    Comment selectByPrimaryKey(Integer commentID);

    List<Comment> selectAll();

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByBlogID(Integer blogID);
}