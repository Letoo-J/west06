package com.mine.west.service.impl;

import com.mine.west.dao.AccountMapper;
import com.mine.west.dao.AccountoperationMapper;
import com.mine.west.dao.BlogMapper;
import com.mine.west.dao.PictureMapper;
import com.mine.west.exception.AccountException;
import com.mine.west.exception.BlogException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.exception.ModelException;
import com.mine.west.modelLegal.BlogLegal;
import com.mine.west.models.Account;
import com.mine.west.models.Accountoperation;
import com.mine.west.models.Blog;
import com.mine.west.models.Picture;
import com.mine.west.service.BlogService2;
import com.mine.west.userBasedCollaborativeFiltering.UserCF;
import com.mine.west.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService2 {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountoperationMapper accountoperationMapper;
    @Autowired
    private PictureMapper pictureMapper;

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
    public int repost(Integer accountID, Integer blogID) throws BlogException {
        Blog blog = blogMapper.selectByPrimaryKey(blogID);
        if (blog == null)
            throw new BlogException(ExceptionInfo.BLOG_ID_NOT_EXIT);
        blog.setRepostNumber(blog.getRepostNumber() + 1);
        blogMapper.updateByPrimaryKey(blog);
        Accountoperation accountoperation = accountoperationMapper.select(accountID, blogID);
        if (accountoperation == null) {
            accountoperationMapper.insert(new Accountoperation(0, accountID, blogID, repostWeight));
        } else {
            accountoperation.setInterest(accountoperation.getInterest() + repostWeight);
            accountoperationMapper.updateByPrimaryKey(accountoperation);
        }
        return blog.getRepostNumber();
    }

    @Override
    public boolean createPicture(File file, Integer blogID) throws AccountException {
        String ext = (file.getName().substring(file.getName().lastIndexOf(".") + 1)).toLowerCase();
        if ((!ext.equals("jpg")) && (!ext.equals("png")))
            throw new AccountException(ExceptionInfo.ACCOUNT_AVATAR_ILLEGAL);
        if (!ImageUtil.PictureIsLegal(file))
            throw new AccountException(ExceptionInfo.ACCOUNT_AVATAR_ILLEGAL);

        ImageUtil.waterMarkByText("picture", file.getAbsolutePath(), file.getAbsolutePath(), 3, 100, 100, 0F);
        pictureMapper.insertPicture(new Picture(file.getName(), 0, blogID));

        return false;
    }

    @Override
    public byte[][] readPicture(String headPath, Integer blogID) {
        try {
            byte[][] pictureSet = new byte[10][];

            List<Picture> pictureList = pictureMapper.readByBlogID(blogID);
            if (pictureList != null) {
                for (int i = 0; (i < 9) && (i < pictureList.size()); i++) {
                    File f = new File(headPath, pictureList.get(i).getPictureName());

                    System.out.println(f.getAbsolutePath());

                    BufferedImage bi = ImageIO.read(f);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(bi, "png", os);
                    pictureSet[i] = os.toByteArray();
                }
            }

            return pictureSet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
