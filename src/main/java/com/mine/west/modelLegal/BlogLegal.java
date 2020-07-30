package com.mine.west.modelLegal;

import com.mine.west.exception.AccountException;
import com.mine.west.exception.BlogException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.models.Blog;

public class BlogLegal extends BaseLegal {

    public static boolean blogIDLegal(Integer blogID) {
        if (blogID < 1)
            return false;
        return true;
    }

    public static boolean contentLegal(String content) {
        if (stringIsEmpty(content))
            return true;
        if (content.length() >= TEXT_MAX_SIZE)
            return false;
        return true;
    }

    public static void isAllLegal(Blog blog) throws BlogException, AccountException {
        if (!AccountLegal.accountIDLegal(blog.getAccountID()))
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_ILLEGAL);
        if (!contentLegal(blog.getContent()))
            throw new BlogException(ExceptionInfo.BLOG_CONTENT_ILLEGAL);
    }

    public static void isEmpty(Blog blog) throws BlogException {
        if (stringIsEmpty(blog.getContent()))
            throw new BlogException(ExceptionInfo.BLOG_CONTENT_EMPTY);
    }
}
