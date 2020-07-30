package com.mine.west.dao;

import com.mine.west.models.Comment;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer commentID);

    int insert(Comment record);

    Comment selectByPrimaryKey(Integer commentID);

    List<Comment> selectAll();

    int updateByPrimaryKey(Comment record);

    List<Comment> selectByBlogID(Integer blogID);
}