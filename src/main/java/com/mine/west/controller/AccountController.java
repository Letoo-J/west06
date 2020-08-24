package com.mine.west.controller;

<<<<<<< HEAD
import com.mine.west.config.shiro.AccountToken;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.service.impl.RegisterServiceImpl;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping(value = "/account")
public class AccountController {
    @Autowired
    private RegisterServiceImpl _registerService;

    @Autowired
    private AccountService _accountService;

    /**
     * 用户登录
     * @param session
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    @PostMapping("/login")
    public AjaxResponse login(HttpSession session, String username, String password,
                              @Param("rememberMe")String rememberMe){
        boolean isRememberMe = false;
        if(rememberMe != null ) {
            isRememberMe = true;
        }

        //初始化自定义token
        AccountToken token = new AccountToken(username, password,isRememberMe);
        //获取主体对象
        Subject subject = SecurityUtils.getSubject();
        try {
            log.info("获取到信息，开始验证！！");
            //进行登录验证
            subject.login(new UsernamePasswordToken(username,password));
            //获取登录的用户信息
            Account account = (Account) subject.getPrincipal();
            //登陆成功的话，用户信息放到session中
            session.setAttribute("account", account);
            //放入csrf-token:
            //String uuidToken = (String) session.getAttribute("uuidToken");
            //String uuidToken = UUID.randomUUID().toString();
            //session.setAttribute("uuidToken",uuidToken );

            log.info("登陆成功！");
            return AjaxResponse.success("登陆成功！");

        } catch (AuthenticationException e){
            log.warn(e.getMessage());
            return AjaxResponse.fail(500,e.getMessage());
        } /*catch (UnknownAccountException e) {
            e.printStackTrace();
            return AjaxResponse.fail(500,"用户名错误!");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            return AjaxResponse.fail(500,"密码错误!");
        }*/
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @param mail
     * @param verifyInput
     * @return
     */
    @PostMapping("/register")//注册
    public AjaxResponse register(String username,String password,String mail,String verifyInput){

        String verifyInput02 = verifyInput.toUpperCase(); //转换为大写

        String msg = _registerService.register(username,password,mail,verifyInput02);
        if(msg.equals("注册成功")){
            return AjaxResponse.success(msg);
        }
        else if(msg.equals("注册失败....有一股神秘力量.....")){
            return AjaxResponse.fail(500,msg);
        }
        return AjaxResponse.fail(400,msg);
    }

    /**
     * 验证用户名是否存在
     * @param username
     * @return
     *     对应前端的remote中的URL地址
     *     远程地址只能输出 "true" 或 "false"，不能有其他输出!
     */
    @RequestMapping("/register/validateUsername")
    public boolean validateUsername(@Param("username") String username){
        Account u = _accountService.selectAccountByName(username);
        if(u == null){  //不存在此用户名
            return true;
        }
        return false;
    }

    /**
     * 验证邮箱是否存在
     * @param mail
     * @return
     */
    @RequestMapping("/register/validateEMail")
    public boolean validateEMail(@Param("mail") String mail){
        Account u = _accountService.selectAccountByMailbox(mail);
        if(u == null){  //不存在此用户邮箱
            return true;
        }
        return false;
    }


    /**
     * 退出系统
     * @return
     */
    @RequestMapping("/logout")
    public AjaxResponse logout(){
        Subject subject = SecurityUtils.getSubject();//取出当前验证主体
        if (subject != null) {
            subject.logout(); //不为空，执行一次logout的操作，将session全部清空
        }
        return AjaxResponse.success("退出成功");   //重定向到“/login”
    }
=======
import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/account")
public class AccountController {
    private static String HEAD_PATH;

    static {
        //TODO : 修改文件地址，目前文件夹位置在桌面
//        HEAD_PATH = ResourceUtils.getURL("classpath:").getPath()+"avatar/";
        HEAD_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "\\avatar\\";
    }

    @Autowired
    AccountService accountService;

    @GetMapping("/{accountID}")
    public AjaxResponse getAccount(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(accountService.getAccount(accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    @GetMapping("/avatar/{accountID}")
    public AjaxResponse getAvatar(@PathVariable("accountID") Integer accountID,
                                  HttpServletRequest request) {
        try {
            return AjaxResponse.success(accountService.getAvatar(HEAD_PATH, accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    @PutMapping
    public AjaxResponse updateAccount(@RequestBody Account account) {
        try {
            return AjaxResponse.success(accountService.updateAccount(account));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    @PutMapping("/avatar/{accountID}")
    public AjaxResponse updateAvatar(@RequestParam("avatar") MultipartFile avatar,
                                     @PathVariable("accountID") Integer accountID) {
        File file = null;
        try {
            String filePath = HEAD_PATH + (new Date()).getTime() + avatar.getOriginalFilename();
            file = new File(filePath);
            avatar.transferTo(file);
            return AjaxResponse.success(accountService.updateAvatar(file, accountID));
        } catch (ModelException | IOException e) {
            if (file != null)
                file.delete();//删除有问题的图片
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

>>>>>>> 16335e4c16308fbe28b2fcb6a50239ef43436357
}
