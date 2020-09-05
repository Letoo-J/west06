package com.mine.west.controller;

import com.mine.west.exception.AccountException;
import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.Comment;
import com.mine.west.models.CommentAll;
import com.mine.west.service.AccountServiceT;
import com.mine.west.service.CommentService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/comment")
@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    AccountServiceT accountService;

    /**
     * 发布评论
     *
     * @param comment
     * @return 评论ID
     */
    @RequestMapping(method = RequestMethod.POST)
    public AjaxResponse create(@RequestBody Comment comment, HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            comment.setLikeNumber(0);
            comment.setCommentTime(new Date());
            comment.setAccountID(account.getAccountID());
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
    @ApiResponses({
            @ApiResponse(code = 200, message = "评论列表，博客下所有评论，单个评论如下", response = Comment.class)
    })
    @RequestMapping(value = "/{blogID}", method = RequestMethod.GET)
    public AjaxResponse readeByBlockID(@PathVariable("blogID") Integer blogID) {
        try {
            List<Comment> commentList = commentService.readeByBlockID(blogID);
            List<CommentAll> allList = new ArrayList<>();
            int k;
            for (Comment c : commentList) {
                Account account1 = accountService.getAccount(c.getAccountID());
                k = c.getReceivedID();
                if (k != 0) {
                    Account account2 = accountService.getAccount(c.getReceivedID());
                    allList.add(new CommentAll(c, account1.getName(), account2.getName()));
                    continue;
                }
                allList.add(new CommentAll(c, account1.getName(), null));
            }
            return AjaxResponse.success(allList);
        } catch (AccountException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 给评论点赞
     *
     * @param commentID
     * @return 当前点赞数
     */
    @RequestMapping(value = "/like/{commentID}", method = RequestMethod.PUT)
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
    @RequestMapping(value = "/{commentID}", method = RequestMethod.DELETE)
    public AjaxResponse delete(@PathVariable("commentID") Integer commentID) {
        try {
            return AjaxResponse.success(commentService.delete(commentID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }
}
