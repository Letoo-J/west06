package com.mine.west.service;

import com.mine.west.models.Blog;

public interface BlogService {
    /**
     * 发布一篇博客
     * @param blog
     * @return
     */
    int addBlog(Blog blog);
}
