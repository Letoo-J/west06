package com.mine.west.service;

import com.mine.west.models.Blog;
import org.springframework.stereotype.Service;

public interface BlogService {
    /**
     * 发布一篇博客
     * @param blog
     * @return
     */
    public int addBlog(Blog blog);
}
