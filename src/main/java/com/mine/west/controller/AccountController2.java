package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.service.AccountServiceT;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/accountT")
@RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
public class AccountController2 {
    private static String HEAD_PATH;

    static {
        //TODO : 修改文件地址
        try {
            HEAD_PATH = ResourceUtils.getURL("classpath:").getPath() + "avatar/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        HEAD_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "\\avatar\\";
    }

    @Autowired
    AccountServiceT accountService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "返回的内层数据（即data值）", response = Account.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public AjaxResponse getAccount(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(accountService.getAccount(account.getAccountID()));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "返回的data的值为byte数组，\n" +
                    "前端接收：$('#img').attr('src', \"data:image/png;base64,\" + Image);")
    })
    @RequestMapping(value = "/avatar", method = RequestMethod.GET)
    public AjaxResponse getAvatar(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return AjaxResponse.success(accountService.getAvatar(HEAD_PATH, account.getAccountID()));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

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

    @RequestMapping(value = "/avatar", method = RequestMethod.PUT)
    public AjaxResponse updateAvatar(@RequestParam("avatar") MultipartFile avatar,
                                     HttpSession session) {
        File file = null;
        try {
            Account account = (Account) session.getAttribute("account");
            String filePath = HEAD_PATH + (new Date()).getTime() + avatar.getOriginalFilename();
            file = new File(filePath);
            avatar.transferTo(file);
            return AjaxResponse.success(accountService.updateAvatar(file, account.getAccountID()));
        } catch (ModelException | IOException e) {
            if (file != null) {
                file.delete();//删除有问题的图片
            }
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    /**
     * 修改密码
     * @param session
     * @param password
     * @param newPassword1
     * @param newPassword2
     * @return
     */
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/password",method = RequestMethod.PUT)
    public AjaxResponse updatePassword(HttpSession session,@RequestParam("password")String password,
           @RequestParam("newPassword1")String newPassword1,@RequestParam("newPassword2")String newPassword2) {

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
     * @param password1
     * @param password2
     * @return
     */
    @ApiOperation(value = "第三方登录用户进行初始密码设置")
    @RequestMapping(value = "/TP/password",method = RequestMethod.PUT)
    public AjaxResponse setPassword(HttpSession session,
                  @RequestParam("password1")String password1,@RequestParam("password2")String password2){
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
