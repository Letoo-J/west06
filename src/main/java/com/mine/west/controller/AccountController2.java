package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.AccountAll;
import com.mine.west.service.AccountServiceT;
import com.mine.west.service.BlogService2;
import com.mine.west.service.FollowService;
import com.mine.west.util.AjaxResponse;
import com.mine.west.util.SaltUtils;
import com.mine.west.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/accountT")
@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class AccountController2 {
    public static final String HEAD_PATH = "/root/myhome/src/main/resources/avatar/";

    @Autowired
    AccountServiceT accountService;
    @Autowired
    private BlogService2 blogService;
    @Autowired
    FollowService followService;

    /**
     * 查询用户信息
     *
     * @param session
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回的内层数据（即data值）", response = Account.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public AjaxResponse getAccount(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            Account account1 = accountService.getAccount(account.getAccountID());
            //博客数
            int blogNum = blogService.readBlogNumber(account.getAccountID());
            //用户关注数
            int followNum = followService.readFollowNumber(account.getAccountID());
            //用户粉丝数
            int fanNum = followService.readFanNumber(account.getAccountID());
            return AjaxResponse.success(new AccountAll(account1, blogNum, followNum, fanNum));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 通过ID查询用户信息
     *
     * @param accountID
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回的内层数据（即data值）", response = Account.class)
    })
    @RequestMapping(value = "/read/{accountID}", method = RequestMethod.GET)
    public AjaxResponse getAccountByID(@PathVariable("accountID") Integer accountID) {
        try {
            Account account1 = accountService.getAccount(accountID);
            //博客数
            int blogNum = blogService.readBlogNumber(accountID);
            //用户关注数
            int followNum = followService.readFollowNumber(accountID);
            //用户粉丝数
            int fanNum = followService.readFanNumber(accountID);
            return AjaxResponse.success(new AccountAll(account1, blogNum, followNum, fanNum));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 查询头像
     *
     * @param accountID
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "返回的data的值为byte数组，\n" +
                    "前端接收：$('#img').attr('src', \"data:image/png;base64,\" + Image);")
    })
    @RequestMapping(value = "/avatar/{accountID}", method = RequestMethod.GET)
    public AjaxResponse getAvatar(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(accountService.getAvatar(HEAD_PATH, accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 更新用户信息
     *
     * @param account
     * @param session
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public AjaxResponse updateAccount(@RequestBody Account account,
                                      HttpSession session) {
        try {
            Account account0 = (Account) session.getAttribute("account");
            account.setAccountID(account0.getAccountID());
            return AjaxResponse.success(accountService.updateAccount(account));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 更新头像
     *
     * @param avatar
     * @param session
     * @return
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.PUT)
    public AjaxResponse updateAvatar(@RequestParam("avatar") MultipartFile avatar,
                                     HttpSession session) {
        File file = null;
        try {
            Account account = (Account) session.getAttribute("account");
            String filePath = HEAD_PATH + (new Date()).getTime() + avatar.getOriginalFilename();
            file = new File(filePath);
            //将avatar转化为file，并存入file
            avatar.transferTo(file);
            return AjaxResponse.success(accountService.updateAvatar(file, account.getAccountID()));
        } catch (ModelException | IOException e) {
            if (file != null)
                file.delete();//删除有问题的图片
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 修改密码
     * @param session
     * @return
     */
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/password",method = RequestMethod.PUT)
    public AjaxResponse updatePassword(HttpSession session,@RequestBody Map<String, String> pas) {
        /*
           @RequestParam("password")String password,
           @RequestParam("newPassword1")String newPassword1,
           @RequestParam("newPassword2")String newPassword2
         */
        String password = pas.get("password");
        String newPassword1 = pas.get("newPassword1");
        String newPassword2 = pas.get("newPassword2");

        Account account = (Account) session.getAttribute("account");
        String salt = account.getSalt();
        String passwordSalt = account.getPassword();
        boolean b = Account.passwordMatch(password,salt,passwordSalt);
        if(!b){
            return AjaxResponse.fail(400,"密码错误！");
        }
        if(!newPassword1.equals(newPassword2)){
            return AjaxResponse.fail(400,"两次输入密码不一致！");
        }

        String newsalt = SaltUtils.getSalt(10);
        String newPassword = new Md5Hash(newPassword1, newsalt,1023).toHex();
        account.setPassword(newPassword);
        account.setSalt(newsalt);
        try {
            accountService.updateAccount(account);
            log.info("数据库修改密码成功！");
            Subject subject= SecurityUtils.getSubject();
            if (subject != null) {
                subject.logout();  //不为空,执行一次logout的操作，将session全部清空
                log.info("改完密码后，退出成功！");
            }
            return AjaxResponse.success("修改密码成功");
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 第三方登录用户进行初始密码设置
     * @param session
     * @return
     */
    @ApiOperation(value = "第三方登录用户进行初始密码设置")
    @RequestMapping(value = "/TP/password",method = RequestMethod.PUT)
    public AjaxResponse setPassword(HttpSession session,@RequestBody Map<String, String> m){
        /*
        @RequestParam("password1")String password1,@RequestParam("password2")String password2
         */
        String password1 = m.get("password1");
        String password2 = m.get("password2");

        Account account = (Account) session.getAttribute("account");
        //密码为空才可以设置
        if(StringUtils.isEmpty(account.getPassword())){
            if(!password1.equals(password2)){
                return AjaxResponse.fail(400,"两次输入密码不一致！");
            }
            String salt = SaltUtils.getSalt(10);
            String Password = new Md5Hash(password1, salt,1023).toHex();
            account.setPassword(Password);
            account.setSalt(salt);
            try {
                return AjaxResponse.success(accountService.updateAccount(account));
            } catch (ModelException e) {
                return new AjaxResponse(true, 400, e.getMessage(), null);
            }
        }
        return AjaxResponse.success(400,"密码非空，无法设置！");
    }


}
