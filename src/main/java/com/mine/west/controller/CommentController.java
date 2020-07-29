package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Comment;
import com.mine.west.service.CommentService;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 发布评论
     *
     * @param comment
     * @return 评论ID
     */
    @PostMapping
    public AjaxResponse create(@RequestBody Comment comment) {
        try {
            comment.setLikeNumber(0);
            comment.setCommentTime(new Date());
            return AjaxResponse.success(commentService.create(comment));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 查询某个博客的所有评论
     *
     * @return
     */
    @GetMapping("/{blogID}")
    public AjaxResponse readeByBlockID(@PathVariable("blogID") Integer blogID) {
        return AjaxResponse.success(commentService.readeByBlockID(blogID));
    }

    /**
     * 给评论点赞
     *
     * @param commentID
     * @return 当前点赞数
     */
    @PutMapping("/{commentID}")
    public AjaxResponse like(@PathVariable("commentID") Integer commentID) {
        try {
            return AjaxResponse.success(commentService.like(commentID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 删除评论
     *
     * @param commentID
     * @return true为删除成功
     */
    @DeleteMapping("/{commentID}")
    public AjaxResponse delete(@PathVariable("commentID") Integer commentID) {
        try {
            return AjaxResponse.success(commentService.delete(commentID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }
}
