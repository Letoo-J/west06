package com.mine.west.service.impl;

<<<<<<< HEAD
import com.mine.west.constant.AccountConstants;
import com.mine.west.dao.AccountMapper;
import com.mine.west.models.Account;
import com.mine.west.service.AccountService;
import com.mine.west.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
=======
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
>>>>>>> 16335e4c16308fbe28b2fcb6a50239ef43436357

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
<<<<<<< HEAD
    public int deleteByPrimaryKey(Integer accountID) {
        return accountMapper.deleteByPrimaryKey(accountID);
    }

    @Override
    public int insertAccount(Account record) {
        return accountMapper.insertAccount(record);
    }

    @Override
    public int updateByPrimaryKey(Account record) {
        return accountMapper.updateByPrimaryKey(record);
    }

    @Override
    public Account selectByPrimaryKey(Integer accountID) {
        return accountMapper.selectByPrimaryKey(accountID);
    }

    @Override
    public List<Account> selectAll() {
        return accountMapper.selectAll();
    }

    @Override
    public List<Account> selectAccountLikeName(String name) {
        return accountMapper.selectAccountLikeName(name);
    }

    @Override
    public Account selectAccountByName(String name) {
        return accountMapper.selectAccountByName(name);
    }

    @Override
    public Account selectAccountByMailbox(String mailbox) {
        return accountMapper.selectAccountByMailbox(mailbox);
    }

    @Override
    public String checkNameUnique(String name) {
        int count = accountMapper.checkNameUnique(name);
        if (count > 0)
        {
            return AccountConstants.USER_NAME_NOT_UNIQUE; //1
        }
        return AccountConstants.USER_NAME_UNIQUE; //0
    }

    @Override
    public String checkMailboxUnique(Account record) {
        Integer accountId = (int) (StringUtils.isNull(record.getAccountID()) ? -1 : record.getAccountID());
        Account info = accountMapper.checkMailboxUnique(record.getMailbox());
        if (StringUtils.isNotNull(info) && info.getAccountID() != accountId )
        {
            return AccountConstants.USER_EMAIL_NOT_UNIQUE;  //1
        }
        return AccountConstants.USER_EMAIL_UNIQUE;  //0
    }


}

=======
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
>>>>>>> 16335e4c16308fbe28b2fcb6a50239ef43436357
