package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.Blog;
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

import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/blog")
@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class BlogController2 {

    @Autowired
    private BlogService2 blogService;

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
            return AjaxResponse.success(blogService.readByAccountID(account.getAccountID()));
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
            return AjaxResponse.success(blogService.readAll(account.getAccountID()));
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

}
