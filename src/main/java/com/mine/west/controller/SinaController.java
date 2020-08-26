package com.mine.west.controller;

import com.alibaba.fastjson.JSONObject;
import com.mine.west.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO ： 如果将项目部署到服务器上，需要修改REDIRECT_URI
 */
@Slf4j
@Controller
@RequestMapping(value = "/sina")
public class SinaController {
    //查看文档：https://open.weibo.com/wiki/Oauth2/access_token
    private static final String REDIRECT_URI = "http://127.0.0.1:8080/sina/callback";
    private static final String CLIENT_ID = "3307968672";
    private static final String CLIENT_SECRET = "7dc91961086ffb68c7607e9b4346c19e";
    private static final String GET_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    private static final String GET_ACCOUNT_URL = "https://api.weibo.com/2/users/show.json";

    @RequestMapping(value = "/callback", method = {RequestMethod.GET})
    public String callback(String code, Model model) {
        Map<String, String> params = new HashMap<>(5);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("redirect_uri", REDIRECT_URI);
        String result = getMessage(GET_TOKEN_URL, params);

        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        String getUserInfo = getAccount(jsonObject.get("access_token"), jsonObject.get("uid"));
        //返回的信息里面包含用户的信息
        //具体信息查看https://open.weibo.com/wiki/2/users/show
        jsonObject = (JSONObject) JSONObject.parse(getUserInfo);

        //取出用户名
        model.addAttribute("name", jsonObject.get("name"));

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
