package com.mine.west.controller;

import com.mine.west.email.MailboxVerificationUtil;
import com.mine.west.exception.AccountException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/hello")
public class TestController {

    @GetMapping
    public String hello() throws AccountException {
        MailboxVerificationUtil.sendEmail("690054845@qq.com");
        return "hello World~~~~~";
    }

    @GetMapping("/{code}")
    public String helloT(@PathVariable("code") String code) throws AccountException {
        if (MailboxVerificationUtil.verificationCodeIsLegal(code, "690054845@qq.com"))
            return "ok";
        return "hello World~~~~~";
    }

    //@RequiresAuthentication
    //@RequiresRoles(value = {"user","admin"},logical = Logical.OR)
    @RequiresPermissions(value = {"user:*:*"})
    @GetMapping("/1111")
    public String hello02() throws AccountException {
        return "hello World~~~~~";
    }
}
