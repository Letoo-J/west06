package com.mine.west.controller;

import com.mine.west.config.shiro.AccountToken;
import com.mine.west.constant.ResultStatusCode;
import com.mine.west.email.MailboxVerificationUtil;
import com.mine.west.exception.AccountException;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.service.impl.RegisterServiceImpl;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping(value = "/login")
    public AjaxResponse login(HttpSession session, String username,
                              String password, String rememberMe) {
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
            subject.login(token);
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
    @PostMapping(value = "/register")
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
    @GetMapping("/register/validateUsername")
    public boolean validateUsername(@Param("username") String username){
        Account u = _accountService.selectAccountByName(username);
        if(u == null){  //不存在此用户名
            return true;
        }
        return false;
    }

    /**
     * 验证邮箱是否存在
     * @param mailBox
     * @return
     */
    @GetMapping("/register/validateEMail")
    public boolean validateEMail(@Param("mailBox") String mailBox){
        Account u = _accountService.selectAccountByMailbox(mailBox);
        if(u == null){  //不存在此用户邮箱
            return true;
        }
        return false;
    }


    /**
     * 退出系统
     * @return
     */
    @PostMapping("/logout")
    public AjaxResponse logout(){
        Subject subject = SecurityUtils.getSubject();//取出当前验证主体
        if (subject != null) {
            subject.logout(); //不为空，执行一次logout的操作，将session全部清空
        }
        return AjaxResponse.success("退出成功");   //重定向到“/login”
    }

    /**
     * 向邮箱发送验证码
     * @param mailBox
     * @return
     */
    @GetMapping("/register/emaliVerification")
    public AjaxResponse emaliVerification(@Param("mailBox") String mailBox){
        try {
            boolean b = MailboxVerificationUtil.sendEmail(mailBox);
        } catch (AccountException e) {
            e.printStackTrace();
        }
        return AjaxResponse.success("邮箱验证码发送成功！");
    }


    /**
     * 未授权跳转方法
     * @return
     */
    @RequestMapping("/unauth")
    public AjaxResponse unauth(){
        SecurityUtils.getSubject().logout();
        return AjaxResponse.fail(ResultStatusCode.UNAUTHO_ERROR,"您没有该权限");
    }

    /**
     * 被踢出后跳转方法
     * @return
     */
    @RequestMapping("/kickout")
    public AjaxResponse kickout(){
        return AjaxResponse.fail(ResultStatusCode.INVALID_TOKEN,"您已经被踢出");
    }
}
