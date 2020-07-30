package com.mine.west.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionInfo {
    OK(1000, "OK"),

    BLOG_ID_ILLEGAL(2000, "博客ID非法"),
    BLOG_ID_NOT_EXIT(2001, "博客ID不存在"),
    BLOG_CONTENT_EMPTY(2002, "博客内容为空"),
    BLOG_CONTENT_ILLEGAL(2003, "博客内容非法"),

    COMMENT_ID_ILLEGAL(3000, "评论ID非法"),
    COMMENT_CONTENT_ILLEGAL(3001, "评论内容非法"),
    COMMENT_CONTENT_EMPTY(3002, "评论内容为空"),
    COMMENT_ID_NOT_EXIST(3003, "评论ID不存在"),

    ACCOUNT_ID_NOT_EXIST(4000, "用户ID不存在"),
    ACCOUNT_ID_ILLEGAL(4001, "用户ID非法"),

    FOLLOW_EXIT(5000, "不能重复关注"),
    FOLLOW_NOT_EXIT(5001, "已取消关注");

    private int code;
    private String message;
}
