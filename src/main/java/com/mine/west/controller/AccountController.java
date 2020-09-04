package com.mine.west.controller;

import com.mine.west.config.shiro.AccountToken;
import com.mine.west.constant.ResultStatusCode;
import com.mine.west.email.MailboxVerificationUtil;
import com.mine.west.exception.AccountException;
import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.models.LoginAccount;
import com.mine.west.models.RegisterAccount;
import com.mine.west.service.AccountService;
import com.mine.west.service.AccountServiceT;
import com.mine.west.service.impl.RegisterServiceImpl;
import com.mine.west.util.AjaxResponse;
import com.mine.west.util.SaltUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/account")
public class AccountController {
    @Autowired
    private RegisterServiceImpl _registerService;

    @Autowired
    private AccountService _accountService;
    @Autowired
    private AccountServiceT accountService;

    /**
     * 用户登录
     * 登录成功则返回sessionId作为token给前端存储，前端请求时将该token放入请求头，
     * 以Authorization为key，以此来鉴权。如果出现账号或密码错误等异常则返回错误信息。
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)  //Post
    public AjaxResponse login(@RequestBody LoginAccount ac, HttpSession session) {
//    public AjaxResponse login(HttpSession session, @RequestParam("username")String username,
//                              @RequestParam("password")String password, @RequestParam("rememberMe")String rememberMe){
        String username = ac.getUsername();
        String password = ac.getPassword();
        String rememberMe = ac.getRememberMe();


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
            session.setAttribute("token",subject.getSession().getId());
            //放入csrf-token:
            //String uuidToken = (String) session.getAttribute("uuidToken");
            //String uuidToken = UUID.randomUUID().toString();
            //session.setAttribute("uuidToken",uuidToken );

            log.info("登陆成功！");
            return AjaxResponse.success("登陆成功！");

        } catch (AuthenticationException e){
            log.warn("登录失败："+e.getMessage());
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
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public AjaxResponse register(@RequestBody RegisterAccount ac) {
        String username = ac.getUsername();
        String password = ac.getPassword();
        String mail = ac.getMail();
        String verifyInput = ac.getVerifyInput();

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
    @ApiOperation(value = "验证用户名是否存在")
    @RequestMapping(value = "/register/validateUsername",method = RequestMethod.GET)
    public boolean validateUsername(@RequestParam("username")String username){
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
    @ApiOperation(value = "验证邮箱是否存在")
    @RequestMapping(value = "/register/validateEMail",method = RequestMethod.GET)
    public boolean validateEMail(@RequestParam("mailBox") String mailBox){
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
    @ApiOperation(value = "退出系统")
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public AjaxResponse logout(){
        log.info("【执行logout！】");
        Subject subject = SecurityUtils.getSubject();//取出当前验证主体
        if (subject != null) {
            subject.logout(); //不为空，执行一次logout的操作，将session全部清空
            return AjaxResponse.success("退出成功");
        }
        return AjaxResponse.fail(500,"退出失败");
    }


    /**
     * 未授权跳转方法
     * @return
     */
    @ApiOperation(value = "未登录跳转方法")
    @RequestMapping(value = "/unauth",method = RequestMethod.GET)
    public AjaxResponse unauth(){
        SecurityUtils.getSubject().logout();
        return AjaxResponse.fail(ResultStatusCode.UNAUTHO_ERROR,"您没有登录");
    }

    /**
     * 被踢出后跳转方法
     * @return
     */
    @ApiOperation(value = "被踢出后跳转方法")
    @RequestMapping(value = "/kickout",method = RequestMethod.GET)
    public AjaxResponse kickout(){
        return AjaxResponse.fail(ResultStatusCode.INVALID_TOKEN,"您已经被踢出");
    }

    /**
     * 向邮箱发送验证码
     * @param mailBox
     * @return
     */
    @ApiOperation(value = "向邮箱发送验证码", notes = "向邮箱发送验证码")
    @RequestMapping(value = "/send/emaliVerification",method = RequestMethod.GET)
    public AjaxResponse emaliVerification(@RequestParam("mailBox") String mailBox){
        try {
            boolean b = MailboxVerificationUtil.sendEmail(mailBox);
        } catch (AccountException e) {
            e.printStackTrace();
        }
        return AjaxResponse.success("邮箱验证码发送成功！");
    }

    /**
     * 找回密码 ：验证邮箱验证码
     * @return
     */
    @ApiOperation(value = "找回密码 ：1.验证邮箱验证码", notes = "找回密码 ：1.验证邮箱验证码")
    @RequestMapping(value = "/find/emaliVerification",method = RequestMethod.GET)
    public AjaxResponse emaliVerificationMatched(@RequestParam("email")String email,@RequestParam("verifyInput")String verifyInput){
        /*

         @RequestBody Map<String, String> m
         */
//        String email = m.get("email");
//        String verifyInput = m.get("verifyInput");
        boolean b = false;
        // 验证码校验
        try {
            b = MailboxVerificationUtil.verificationCodeIsLegal(verifyInput,email);
        } catch (AccountException e) {
            e.printStackTrace();
        }

        if(!b){  //校验不成功
            return AjaxResponse.fail(400,"验证码不正确");
        }
        return AjaxResponse.success("邮箱验证码 验证成功");
    }

    /**
     * 找回密码 ： 2.重置密码
     * @param session
     * @param m
     * @return
     */
    @ApiOperation(value = "找回密码 ： 2.重置密码", notes = "找回密码 ： 2.重置密码")
    @RequestMapping(value = "/find/password",method = RequestMethod.PUT)
    public AjaxResponse updatePassword(HttpSession session, @RequestBody Map<String, String> m) {
        /*
        @RequestParam("newPassword1")String newPassword1,
        @RequestParam("newPassword2")String newPassword2,@RequestParam("email")String email
         */
        String newPassword1 = m.get("newPassword1");
        String newPassword2 = m.get("newPassword2");
        String email = m.get("email");

        Account account = _accountService.selectAccountByMailbox(email);
        if(!newPassword1.equals(newPassword2)){
            return AjaxResponse.fail(400,"两次输入密码不一致！");
        }

        String newsalt = SaltUtils.getSalt(10);
        String newPassword = new Md5Hash(newPassword1, newsalt,1023).toHex();
        account.setPassword(newPassword);
        account.setSalt(newsalt);
        try {
            accountService.updateAccount(account);
            Subject subject= SecurityUtils.getSubject();
            subject.logout();
            return AjaxResponse.success("密码重置成功");
            /*//退出并清理缓存
            Subject subject= SecurityUtils.getSubject();
            subject.logout();
            //自定义清除缓存
            Cache<Object,AuthenticationInfo> cache=systemUserRealm.getAuthenticationCache();
            if (cache!=null){
                cache.remove(sysUserDO.getUsername());
            }*/
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

}
