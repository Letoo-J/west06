package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.service.AccountServiceT;
import com.mine.west.util.AjaxResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
}
