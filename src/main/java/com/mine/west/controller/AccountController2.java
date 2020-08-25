package com.mine.west.controller;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Account;
import com.mine.west.service.AccountServiceT;
import com.mine.west.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Controller
@ResponseBody
@RequestMapping(value = "/accountT")
public class AccountController2 {
    private static String HEAD_PATH;

    static {
        //TODO : 修改文件地址，目前文件夹位置在桌面
//        HEAD_PATH = ResourceUtils.getURL("classpath:").getPath()+"avatar/";
        HEAD_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + "\\avatar\\";
    }

    @Autowired
    AccountServiceT accountService;

    @GetMapping("/{accountID}")
    public AjaxResponse getAccount(@PathVariable("accountID") Integer accountID) {
        try {
            return AjaxResponse.success(accountService.getAccount(accountID));
        } catch (ModelException e) {
            return new AjaxResponse(true, 400, e.getMessage(), null);
        }
    }

    @GetMapping("/avatar/{accountID}")
    public AjaxResponse getAvatar(@PathVariable("accountID") Integer accountID) {
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
}
