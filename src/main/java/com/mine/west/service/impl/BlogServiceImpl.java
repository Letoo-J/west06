package com.mine.west.service.impl;

import com.mine.west.dao.AccountMapper;
import com.mine.west.dao.AccountoperationMapper;
import com.mine.west.dao.BlogMapper;
import com.mine.west.exception.AccountException;
import com.mine.west.exception.BlogException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.exception.ModelException;
import com.mine.west.modelLegal.BlogLegal;
import com.mine.west.models.Account;
import com.mine.west.models.Accountoperation;
import com.mine.west.models.Blog;
import com.mine.west.service.BlogService2;
import com.mine.west.userBasedCollaborativeFiltering.UserCF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService2 {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountoperationMapper accountoperationMapper;

    public static final float likeWeight = 2L;
    public static final float repostWeight = 4L;

    @Override
    public int create(Blog blog) throws ModelException {
        BlogLegal.isEmpty(blog);
        BlogLegal.isAllLegal(blog);
        Account account = accountMapper.selectByPrimaryKey(blog.getAccountID());
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        blogMapper.insert(blog);
        return blog.getBlogID();
    }

    @Override
    public List<Blog> readByAccountID(Integer accountID) throws ModelException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        return blogMapper.selectByAccountID(accountID);
    }

    @Override
    public int readBlogNumber(Integer accountID) throws ModelException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        return (blogMapper.selectByAccountID(accountID)).size();
    }

    @Override
    public boolean delete(Integer blogID) throws ModelException {
        Blog blog = blogMapper.selectByPrimaryKey(blogID);
        if (blog == null)
            throw new BlogException(ExceptionInfo.BLOG_ID_NOT_EXIT);
        blogMapper.deleteByPrimaryKey(blogID);
        return true;
    }

    @Override
    public List<Blog> readAll(Integer accountID) throws ModelException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        return (new UserCF(blogMapper.selectAll(), accountoperationMapper.selectAll())).getResult(accountID);
    }

    @Override
    public int like(Integer accountID, Integer blogID) throws ModelException {
        Blog blog = blogMapper.selectByPrimaryKey(blogID);
        if (blog == null)
            throw new BlogException(ExceptionInfo.BLOG_ID_NOT_EXIT);
        blog.setLikeNumber(blog.getLikeNumber() + 1);
        blogMapper.updateByPrimaryKey(blog);
        Accountoperation accountoperation = accountoperationMapper.select(accountID, blogID);
        if (accountoperation == null) {
            accountoperationMapper.insert(new Accountoperation(0, accountID, blogID, likeWeight));
        } else {
            accountoperation.setInterest(accountoperation.getInterest() + likeWeight);
            accountoperationMapper.updateByPrimaryKey(accountoperation);
        }
        return blog.getLikeNumber();
    }

    @Override
    public int repost(Integer blogID) {
        //TODO : 待开发
        return 0;
    }
}
