package com.mine.west.controller;

import com.alibaba.fastjson.JSONObject;
import com.mine.west.config.shiro.AccountToken;
import com.mine.west.models.Account;
import com.mine.west.service.impl.RegisterServiceImpl;
import com.mine.west.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO ： 如果将项目部署到服务器上，需要修改REDIRECT_URI
 */
@Slf4j
@Controller
@RequestMapping(value = "/sina")
public class SinaController {
    @Autowired
    private RegisterServiceImpl _registerService;

    //查看文档：https://open.weibo.com/wiki/Oauth2/access_token
    private static final String REDIRECT_URI = "https://39.101.199.3:443/sina/callback";
    private static final String CLIENT_ID = "3307968672"; //可以使用basic方式加入header中
    private static final String CLIENT_SECRET = "7dc91961086ffb68c7607e9b4346c19e";  //可以使用basic方式加入header中
    private static final String GET_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    private static final String GET_ACCOUNT_URL = "https://api.weibo.com/2/users/show.json";

    @RequestMapping(value = "/callback", method = {RequestMethod.GET})
    public String callback(HttpSession session, String code, Model model) {
        Map<String, String> params = new HashMap<>(5);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("redirect_uri", REDIRECT_URI);

        /**
         * 1.访问Oauth2/access token接口，换取Access Token，返回值JSON
         * {
         *     "access_token": "SlAV32hkKG",
         *     "remind_in": 3600,
         *     "expires_in": 3600,
         *     "uid":"12341234"
         * }
         */
        String result = getMessage(GET_TOKEN_URL, params);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);

        /**
         * 2.访问users/show接口，获取用户信息，返回JSON
         *   返回的信息里面包含用户的信息
         *   具体信息查看https://open.weibo.com/wiki/2/users/show
         */
        String getUserInfo = getAccount(jsonObject.get("access_token"), jsonObject.get("uid"));
        jsonObject = (JSONObject) JSONObject.parse(getUserInfo);


        //获取微博用户的idstr，作为注册的name
        String idstr = (String)jsonObject.get("idstr");
        /**
         * 进行第三方注册：
         */
        Account account02 = _registerService.TPregister(idstr);

        /**
         * 进行第三方登录：
         */
        //初始化自定义token
        AccountToken token = new AccountToken(idstr, null,false);
        //获取主体对象
        Subject subject = SecurityUtils.getSubject();
        try {
            log.info("第三方登录-开始验证！！");
            //进行登录验证
            subject.login(token);
            //获取登录的用户信息
            Account account = (Account) subject.getPrincipal();
            //登陆成功的话，用户信息放到session中
            session.setAttribute("account", account);

            log.info("登陆成功！");
            model.addAttribute("msg", "登陆成功！");

        } catch (AuthenticationException e){
            log.warn(e.getMessage());
            model.addAttribute("msg", e.getMessage());
        }
        //放入用户名
        model.addAttribute("name", jsonObject.get("name"));
        //放入idstr
        model.addAttribute("idstr", idstr);

        //登录成功操作
        //TODO : 登录成功
        //TODO : 登录成功的操作
        //...

        //返回生成前端界面，放在templates文件夹里
        return "weiboSuccess";
    }

    private static String getAccount(Object token, Object uid) {
        StringBuilder stringBuilder = new StringBuilder(GET_ACCOUNT_URL);
        stringBuilder.append("?access_token=");
        stringBuilder.append(token);
        stringBuilder.append("&uid=");
        stringBuilder.append(uid);
        return HttpUtil.getRequest(stringBuilder.toString());
    }

    private static String getMessage(String url, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("?client_id=");
        stringBuilder.append(params.get("client_id"));
        stringBuilder.append("&client_secret=");
        stringBuilder.append(params.get("client_secret"));
        stringBuilder.append("&grant_type=");
        stringBuilder.append(params.get("grant_type"));
        stringBuilder.append("&code=");
        stringBuilder.append(params.get("code"));
        stringBuilder.append("&redirect_uri=");
        stringBuilder.append(params.get("redirect_uri"));
        return HttpUtil.postRequest(stringBuilder.toString());
    }

}
