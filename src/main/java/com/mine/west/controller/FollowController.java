package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.Followlist;
import com.mine.west.service.FollowService;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/followList")
@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class FollowController {
    @Autowired
    FollowService followService;

    /**
     * 读取关注人数
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/follow/number",method = RequestMethod.GET)
    public AjaxResponse readFollowNumber(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(followService.readFollowNumber(account.getAccountID()));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取粉丝人数
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/fan/number",method = RequestMethod.GET)
    public AjaxResponse readFanNumber(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(followService.readFanNumber(account.getAccountID()));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取关注用户
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/follow",method = RequestMethod.GET)
    public AjaxResponse readFollowAccount(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(followService.readFollowAccount(account.getAccountID()));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取粉丝用户
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/fan",method = RequestMethod.GET)
    public AjaxResponse readFanAccount(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(followService.readFanAccount(account.getAccountID()));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 增加关注
     *
     * @param followlist
     * @return
     */
    @PostMapping
    public AjaxResponse create(@RequestBody Followlist followlist) {
        try {
            return AjaxResponse.success(followService.create(followlist));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 取消关注
     *
     * @param fID
     * @return
     */
    @DeleteMapping("/{fID}")
    public AjaxResponse cancelFollow(@PathVariable("fID") Integer fID) {
        try {
            return AjaxResponse.success(followService.cancelFollow(fID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

}
