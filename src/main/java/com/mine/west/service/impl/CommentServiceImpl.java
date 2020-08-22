package com.mine.west.service.impl;

import com.mine.west.dao.AccountoperationMapper;
import com.mine.west.dao.BlogMapper;
import com.mine.west.dao.CommentMapper;
import com.mine.west.exception.BlogException;
import com.mine.west.exception.CommentException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.exception.ModelException;
import com.mine.west.modelLegal.CommentLegal;
import com.mine.west.models.Accountoperation;
import com.mine.west.models.Blog;
import com.mine.west.models.Comment;
import com.mine.west.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private AccountoperationMapper accountoperationMapper;

    public static final float commentWeight = 4L;

    @Override
    public int create(Comment comment) throws ModelException {
        CommentLegal.isEmpty(comment);
        CommentLegal.isAllLegal(comment);
        Blog blog = blogMapper.selectByPrimaryKey(comment.getBlogID());
        if (blog == null)
            throw new BlogException(ExceptionInfo.BLOG_ID_NOT_EXIT);
        blog.setCommentNumber(blog.getCommentNumber() + 1);
        blogMapper.updateByPrimaryKey(blog);
        commentMapper.insert(comment);

        Accountoperation accountoperation = accountoperationMapper.select(comment.getAccountID(), comment.getBlogID());
        if (accountoperation == null) {
            accountoperationMapper.insert(new Accountoperation(0, comment.getAccountID(), comment.getBlogID(), commentWeight));
        } else {
            accountoperation.setInterest(accountoperation.getInterest() + commentWeight);
            accountoperationMapper.updateByPrimaryKey(accountoperation);
        }

        return comment.getCommentID();
    }

    @Override
    public List<Comment> readeByBlockID(Integer blogID) {
        return commentMapper.selectByBlogID(blogID);
    }

    @Override
    public int like(Integer commentID) throws ModelException {
        Comment comment = commentMapper.selectByPrimaryKey(commentID);
        if (comment == null)
            throw new CommentException(ExceptionInfo.COMMENT_ID_NOT_EXIST);
        comment.setLikeNumber(comment.getLikeNumber() + 1);
        commentMapper.updateByPrimaryKey(comment);
        return comment.getLikeNumber();
    }

    @Override
    public boolean delete(Integer commentID) throws ModelException {
        Comment comment = commentMapper.selectByPrimaryKey(commentID);
        if (comment == null)
            throw new CommentException(ExceptionInfo.COMMENT_ID_NOT_EXIST);
        Blog blog = blogMapper.selectByPrimaryKey(comment.getBlogID());
        blog.setCommentNumber(blog.getCommentNumber() - 1);
        blogMapper.updateByPrimaryKey(blog);
        commentMapper.deleteByPrimaryKey(commentID);
        return true;
    }
}
