package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Followlist;
import com.mine.west.service.FollowService;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/followlist")
public class FollowController {
    @Autowired
    FollowService followService;

    /**
     * 读取关注人数
     *
     * @param accountID
     * @return
     */
    @GetMapping("/follow/number/{accountID}")
    public AjaxResponse readFollowNumber(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(followService.readFollowNumber(accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取粉丝人数
     *
     * @param accountID
     * @return
     */
    @GetMapping("/fan/number/{accountID}")
    public AjaxResponse readFanNumber(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(followService.readFanNumber(accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取关注用户
     *
     * @param accountID
     * @return
     */
    @GetMapping("/follow/{accountID}")
    public AjaxResponse readFollowAccount(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(followService.readFollowAccount(accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 读取粉丝用户
     *
     * @param accountID
     * @return
     */
    @GetMapping("/fan/{accountID}")
    public AjaxResponse readFanAccount(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(followService.readFanAccount(accountID));
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
