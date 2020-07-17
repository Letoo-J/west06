package com.mine.west.models;

import java.io.Serializable;

public class Account implements Serializable {
    private Integer accountID;

    private String name;

    private String mailbox;

    private String password;

    private String nickname;

    private String mobilePhone;

    private String sex;

    private String avatar;

    private String individualitySignature;

    private Byte identity;

    private static final long serialVersionUID = 1L;

    public Account(Integer accountID, String name, String mailbox, String password, String nickname, String mobilePhone, String sex, String avatar, String individualitySignature, Byte identity) {
        this.accountID = accountID;
        this.name = name;
        this.mailbox = mailbox;
        this.password = password;
        this.nickname = nickname;
        this.mobilePhone = mobilePhone;
        this.sex = sex;
        this.avatar = avatar;
        this.individualitySignature = individualitySignature;
        this.identity = identity;
    }

    public Account() {
        super();
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox == null ? null : mailbox.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getIndividualitySignature() {
        return individualitySignature;
    }

    public void setIndividualitySignature(String individualitySignature) {
        this.individualitySignature = individualitySignature == null ? null : individualitySignature.trim();
    }

    public Byte getIdentity() {
        return identity;
    }

    public void setIdentity(Byte identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", accountID=").append(accountID);
        sb.append(", name=").append(name);
        sb.append(", mailbox=").append(mailbox);
        sb.append(", password=").append(password);
        sb.append(", nickname=").append(nickname);
        sb.append(", mobilePhone=").append(mobilePhone);
        sb.append(", sex=").append(sex);
        sb.append(", avatar=").append(avatar);
        sb.append(", individualitySignature=").append(individualitySignature);
        sb.append(", identity=").append(identity);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}