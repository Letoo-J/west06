package com.mine.west.service;

import com.mine.west.exception.BlogException;
import com.mine.west.exception.ModelException;
import com.mine.west.models.Blog;

import java.util.List;

public interface BlogService2 {

    /**
     * 增加博客
     *
     * @param blog
     * @return blogID
     * @throws ModelException
     */
    int create(Blog blog) throws ModelException;

    /**
     * 读取某人发布的博客
     *
     * @param accountID
     * @return
     * @throws ModelException
     */
    List<Blog> readByAccountID(Integer accountID) throws ModelException;

    /**
     * 读取发布博客数量
     *
     * @param accountID
     * @return
     * @throws ModelException
     */
    int readBlogNumber(Integer accountID) throws ModelException;

    /**
     * 删除博客
     *
     * @param blogID
     * @return true为成功
     * @throws ModelException
     */
    boolean delete(Integer blogID) throws ModelException;

    /**
     * 读取推荐博客（对所有博客进行排序）
     *
     * @param accountID
     * @return
     * @throws ModelException
     */
    List<Blog> readAll(Integer accountID) throws ModelException;

    /**
     * 点赞
     *
     * @param accountID
     * @param blogID
     * @return 当前点赞数
     * @throws ModelException
     */
    int like(Integer accountID, Integer blogID) throws ModelException;

    /**
     * 转发
     *
     * @param accountID
     * @param blogID
     * @return
     */
    int repost(Integer accountID, Integer blogID) throws BlogException;
}
