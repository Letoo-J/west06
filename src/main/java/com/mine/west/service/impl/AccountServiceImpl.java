package com.mine.west.service.impl;

import com.mine.west.dao.AccountMapper;
import com.mine.west.exception.AccountException;
import com.mine.west.exception.ExceptionInfo;
import com.mine.west.modelLegal.AccountLegal;
import com.mine.west.modelLegal.BaseLegal;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Account getAccount(Integer accountID) throws AccountException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);
        account.setPassword(null);
        return account;
    }

    @Override
    public byte[] getAvatar(String headPath, Integer accountID) throws AccountException {
        try {
            Account account = accountMapper.selectByPrimaryKey(accountID);
            if (account == null)
                throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);

            BufferedImage bi = ImageIO.read(new File(headPath, account.getAvatar()));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", os);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateAccount(Account account) throws AccountException {
        Account ori = accountMapper.selectByPrimaryKey(account.getAccountID());
        if (ori == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);

        //手机
        if (!BaseLegal.stringIsEmpty(account.getMobilePhone())) {
            if (!AccountLegal.phoneLegal(account.getMobilePhone()))
                throw new AccountException(ExceptionInfo.ACCOUNT_PHONE_ILLEGAL);
            ori.setMobilePhone(account.getMobilePhone());
        }

        //昵称
        if (!BaseLegal.stringIsEmpty(account.getNickname())) {
            if (!AccountLegal.nicknameLegal(account.getNickname()))
                throw new AccountException(ExceptionInfo.ACCOUNT_NICKNAME_ILLEGAL);
            ori.setNickname(account.getNickname());
        }

        //性别
        if (!BaseLegal.stringIsEmpty(account.getSex())) {
            if (!AccountLegal.sexLegal(account.getSex()))
                throw new AccountException(ExceptionInfo.ACCOUNT_SEX_ILLEGAL);
            ori.setSex(account.getSex());
        }

        //个性签名
        if (!BaseLegal.stringIsEmpty(account.getIndividualitySignature())) {
            if (!AccountLegal.individualitySignatureLegal(account.getIndividualitySignature()))
                throw new AccountException(ExceptionInfo.ACCOUNT_INDIVIDUALITY_SIGNATURE_ILLEGAL);
            ori.setIndividualitySignature(account.getIndividualitySignature());
        }

        accountMapper.updateByPrimaryKey(ori);

        return true;
    }

    @Override
    public boolean updateAvatar(File avatar, Integer accountID) throws AccountException {
        Account account = accountMapper.selectByPrimaryKey(accountID);
        if (account == null)
            throw new AccountException(ExceptionInfo.ACCOUNT_ID_NOT_EXIST);

        String ext = (avatar.getName().substring(avatar.getName().lastIndexOf(".") + 1)).toLowerCase();
        if ((!ext.equals("jpg")) && (!ext.equals("png")))
            throw new AccountException(ExceptionInfo.ACCOUNT_AVATAR_ILLEGAL);
        if (!ImageUtil.PictureIsLegal(avatar))
            throw new AccountException(ExceptionInfo.ACCOUNT_AVATAR_ILLEGAL);

        ImageUtil.waterMarkByText("avatar", avatar.getAbsolutePath(), avatar.getAbsolutePath(), 3, 100, 100, 0F);
        account.setAvatar(avatar.getName());
        accountMapper.updateByPrimaryKey(account);

        return false;
    }
}
