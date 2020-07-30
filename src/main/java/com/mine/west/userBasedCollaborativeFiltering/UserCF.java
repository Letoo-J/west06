package com.mine.west.userBasedCollaborativeFiltering;

import com.mine.west.models.Blog;

import java.util.List;

/**
 * 基于用户的协同过滤算法
 * TODO : 待开发
 */
public class UserCF {
    private static List<Blog> baseBlog;

    public UserCF(List<Blog> baseBlog) {
        this.baseBlog = baseBlog;
    }

    public static List<Blog> getResult(Integer accountID) {
        return baseBlog;
    }
}
