package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Blog;
import com.mine.west.service.BlogService2;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/blog")
public class BlogController2 {

    @Autowired
    private BlogService2 blogService;

    /**
     * 发布博客
     *
     * @param blog
     * @return blogID
     */
    @PostMapping
    public AjaxResponse create(@RequestBody Blog blog) {
        try {
            blog.setReleaseTime(new Date());
            blog.setLikeNumber(0);
            blog.setCommentNumber(0);
            blog.setRepostNumber(0);
            return AjaxResponse.success(blogService.create(blog));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取某人发布博客
     *
     * @param accountID
     * @return
     */
    @GetMapping("/{accountID}")
    public AjaxResponse read(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(blogService.readByAccountID(accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取某人发布博客数量
     *
     * @param accountID
     * @return
     */
    @GetMapping("/blogNumber/{accountID}")
    public AjaxResponse readNumber(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(blogService.readBlogNumber(accountID));
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
    @DeleteMapping("/{blogID}")
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
     * @param accountID
     * @return
     */
    @GetMapping("/all/{accountID}")
    public AjaxResponse readAll(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(blogService.readAll(accountID));
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
    @PutMapping("/like/{accountID}/{blogID}")
    public AjaxResponse like(@PathVariable("accountID") Integer accountID,
                             @PathVariable("blogID") Integer blogID) {
        try {
            return AjaxResponse.success(blogService.like(accountID, blogID));
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
    @PutMapping("/repost/{blogID}")
    public AjaxResponse repost(@PathVariable("blogID") Integer blogID) {
        return AjaxResponse.success(blogService.repost(blogID));
    }

}
