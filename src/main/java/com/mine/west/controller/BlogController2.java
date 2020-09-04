package com.mine.west.controller;

import com.mine.west.exception.AccountException;
import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.Blog;
import com.mine.west.models.BlogAll;
import com.mine.west.service.AccountServiceT;
import com.mine.west.service.BlogService2;
import com.mine.west.util.AjaxResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/blog")
@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class BlogController2 {
    private static String HEAD_PATH;

    static {
        //TODO : 修改文件地址
        HEAD_PATH = AccountController2.HEAD_PATH;
        //        HEAD_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "\\avatar\\";
    }

    @Autowired
    private BlogService2 blogService;

    @Autowired
    AccountServiceT accountService;

    /**
     * 发布博客
     *
     * @param blog
     * @return blogID
     */
    @RequestMapping(method = RequestMethod.POST)
    public AjaxResponse create(@RequestBody Blog blog, HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            blog.setReleaseTime(new Date());
            blog.setLikeNumber(0);
            blog.setCommentNumber(0);
            blog.setRepostNumber(0);
            blog.setAccountID(account.getAccountID());
            return AjaxResponse.success(blogService.create(blog));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取博客
     *
     * @param session
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "博客列表，用户发布的所有博客\n当个博客如下", response = Blog.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public AjaxResponse read(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            List<Blog> blogList = blogService.readByAccountID(account.getAccountID());
            List<BlogAll> blogAllList = new ArrayList<>();
            for (Blog b : blogList) {
                Account account1 = accountService.getAccount(b.getAccountID());
                blogAllList.add(new BlogAll(b, account1.getName()));
            }
            return AjaxResponse.success(blogAllList);
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取博客数量
     *
     * @param session
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "用户发布博客数量")
    })
    @RequestMapping(value = "/blogNumber", method = RequestMethod.GET)
    public AjaxResponse readNumber(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(blogService.readBlogNumber(account.getAccountID()));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 删除博客
     *
     * @param blogID
     * @return
     */
    @RequestMapping(value = "/{blogID}", method = RequestMethod.DELETE)
    public AjaxResponse delete(@PathVariable("blogID") Integer blogID) {
        try {
            return AjaxResponse.success(blogService.delete(blogID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取所有博客
     *
     * @param session
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "博客列表，所有的博客", response = Blog.class)
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public AjaxResponse readAll(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            List<Blog> blogList = blogService.readAll(account.getAccountID());
            List<BlogAll> blogAllList = new ArrayList<>();
            for (Blog b : blogList) {
                Account account1 = accountService.getAccount(b.getAccountID());
                blogAllList.add(new BlogAll(b, account1.getName()));
            }
            return AjaxResponse.success(blogAllList);
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 给博客点赞
     *
     * @param blogID
     * @return
     */
    @RequestMapping(value = "/like/{blogID}", method = RequestMethod.PUT)
    public AjaxResponse like(HttpSession session,
                             @PathVariable("blogID") Integer blogID) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(blogService.like(account.getAccountID(), blogID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 查询点赞博客
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public AjaxResponse getLike(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            List<Blog> blogList = blogService.getLike(account.getAccountID());
            List<BlogAll> blogAllList = new ArrayList<>();
            for (Blog b : blogList) {
                Account account1 = accountService.getAccount(b.getAccountID());
                blogAllList.add(new BlogAll(b, account1.getName()));
            }
            return AjaxResponse.success(blogAllList);
        } catch (AccountException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 转发博客
     *
     * @param blogID
     * @return
     */
    @RequestMapping(value = "/repost/{blogID}", method = RequestMethod.PUT)
    public AjaxResponse repost(@PathVariable("blogID") Integer blogID,
                               HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(blogService.repost(account.getAccountID(), blogID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 上传博客图片
     *
     * @param blogID
     * @param picture
     * @param session
     * @return
     */
    @RequestMapping(value = "/picture/{blogID}", method = RequestMethod.POST)
    public AjaxResponse createPicture(@PathVariable("blogID") Integer blogID,
                                      @RequestParam("picture") MultipartFile picture,
                                      HttpSession session) {
        File file = null;
        try {
            String filePath = HEAD_PATH + (new Date()).getTime() + picture.getOriginalFilename();
            file = new File(filePath);
            picture.transferTo(file);
            return AjaxResponse.success(blogService.createPicture(file, blogID));
        } catch (ModelException | IOException e) {
            if (file != null) {
                file.delete();//删除有问题的图片
            }
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 查询博客图片
     *
     * @param blogID
     * @return
     */
    @RequestMapping(value = "/picture/{blogID}", method = RequestMethod.GET)
    public AjaxResponse readPicture(@PathVariable("blogID") Integer blogID) {
        return AjaxResponse.success(blogService.readPicture(HEAD_PATH, blogID));
    }

    /**
     * 收藏博客
     *
     * @param session
     * @param blogID
     * @return
     */
    @RequestMapping(value = "/collect/{blogID}", method = RequestMethod.PUT)
    public AjaxResponse collect(HttpSession session,
                                @PathVariable("blogID") Integer blogID) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(blogService.collect(account.getAccountID(), blogID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 查询收藏博客
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/collect", method = RequestMethod.GET)
    public AjaxResponse getCollect(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            List<Blog> blogList = blogService.getCollect(account.getAccountID());
            List<BlogAll> blogAllList = new ArrayList<>();
            for (Blog b : blogList) {
                Account account1 = accountService.getAccount(b.getAccountID());
                blogAllList.add(new BlogAll(b, account1.getName()));
            }
            return AjaxResponse.success(blogAllList);
        } catch (AccountException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

}
