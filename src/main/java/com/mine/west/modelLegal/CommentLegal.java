package com.mine.west.modelLegal;

import com.mine.west.exception.AccountException;
import com.mine.west.exception.BlogException;
import com.mine.west.exception.CommentException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.models.Comment;

public class CommentLegal extends BaseLegal {
    public static boolean contentLegal(String content) {
        if (stringIsEmpty(content))
            return true;
        if (content.length() >= TEXT_MAX_SIZE)
            return false;
        return true;
    }

    public static void isAllLegal(Comment comment) throws BlogException, AccountException, CommentException {
        if (!BlogLegal.blogIDLegal(comment.getBlogID()))
            throw new BlogException(ExceptionInfo.BLOG_ID_ILLEGAL);
        if (!AccountLegal.accountIDLegal(comment.getAccountID()))
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_ILLEGAL);
        if (!contentLegal(comment.getContent()))
            throw new CommentException(ExceptionInfo.COMMENT_CONTENT_ILLEGAL);
    }

    public static void isEmpty(Comment comment) throws CommentException {
        if (stringIsEmpty(comment.getContent()))
            throw new CommentException(ExceptionInfo.COMMENT_CONTENT_EMPTY);
    }
}
