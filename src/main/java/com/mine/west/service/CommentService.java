package com.mine.west.service;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Comment;

import java.util.List;

public interface CommentService {
    /**
     * 添加评论
     *
     * @param comment 评论
     * @return 评论ID
     */
    int create(Comment comment) throws ModelException;

    /**
     * 查询博客所有评论
     *
     * @param blogID 博客ID
     * @return 评论列表
     */
    List<Comment> readeByBlockID(Integer blogID);

    /**
     * 给评论点赞
     *
     * @param commentID 评论ID
     * @return 当前点赞数
     */
    int like(Integer commentID) throws ModelException;

    /**
     * 删除评论
     *
     * @param commentID 评论ID
     * @return true为成功
     */
    boolean delete(Integer commentID) throws ModelException;
}
