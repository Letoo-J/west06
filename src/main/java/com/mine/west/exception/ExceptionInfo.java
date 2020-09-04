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
    BLOG_COLLECTED(2004, "您已收藏了该博客"),
    BLOG_LIKED(2005, "您已点赞了该博客"),

    COMMENT_ID_ILLEGAL(3000, "评论ID非法"),
    COMMENT_CONTENT_ILLEGAL(3001, "评论内容非法"),
    COMMENT_CONTENT_EMPTY(3002, "评论内容为空"),
    COMMENT_ID_NOT_EXIST(3003, "评论ID不存在"),

    ACCOUNT_ID_NOT_EXIST(4000, "用户ID不存在"),
    ACCOUNT_ID_ILLEGAL(4001, "用户ID非法"),
    ACCOUNT_EMAIL_EMPTY(4002, "用户邮箱为空"),
    ACCOUNT_EMAIL_ILLEGAL(4003, "用户邮箱不合法"),
    EMAIL_CODE_ILLEGAL(4004, "邮箱验证码不合法"),
    EMAIL_CODE_EMPTY(4005, "邮箱验证码为空"),
    ACCOUNT_NAME_EXIST(4006, "用户名已存在"),
    ACCOUNT_PHONE_ILLEGAL(4007, "用户手机不合法"),
    ACCOUNT_NICKNAME_ILLEGAL(4008, "用户昵称不合法"),
    ACCOUNT_SEX_ILLEGAL(4009, "用户性别不合法"),
    ACCOUNT_INDIVIDUALITY_SIGNATURE_ILLEGAL(4010, "个性签名不合法"),
    ACCOUNT_AVATAR_ILLEGAL(4011, "用户头像不合法"),

    FOLLOW_EXIT(5000, "不能重复关注"),
    FOLLOW_NOT_EXIT(5001, "已取消关注");

    private int code;
    private String message;
}
