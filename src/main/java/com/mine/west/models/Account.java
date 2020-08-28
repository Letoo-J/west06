package com.mine.west.models;

import lombok.Builder;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.Serializable;
import java.util.List;

/**lombok插件 :
 *  @ AllArgsConstructor  产生全参构造函数（此处已有）
 *  @ NoArgsConstructor   产生无参构造函数（此处已有）  */

/** @ Builder :支持以下构造：
 *  Account aaa = Account.builder()
 *         .accountID("1")
 *         .name("kobe")
 *         .mailbox(39)
 *         .......
 *         .build();
 */
@Builder
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

    private String identity;

    private String salt;

    private String role;

    private static final long serialVersionUID = 1L;

    //定义角色集合
    private List<Roles> roles;

    public Account(Integer accountID, String name, String mailbox, String password, String nickname, String mobilePhone, String sex, String avatar, String individualitySignature, String identity, String salt, String role) {
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
        this.salt = salt;
        this.role = role;
    }

    public Account(Integer accountID, String name, String mailbox, String password, String nickname, String mobilePhone,
                   String sex, String avatar, String individualitySignature, String identity, String salt, String role,List<Roles> roles) {
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
        this.salt = salt;
        this.role = role;
        this.roles = roles;
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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }


    /**
     * 明文密码加盐加密
     * @param password 明文密码
     * @param salt 盐
     * @param passwordSalt 数据库密码
     * @return
     */
    public static Boolean passwordMatch(String password,String salt,String passwordSalt){
        //明文密码加盐加密
        Md5Hash md5Hash = new Md5Hash(password, salt,1023);
        String passwordHash = md5Hash.toHex();
        return passwordHash.equals(passwordSalt);
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
        sb.append(", salt=").append(salt);
        sb.append(", role=").append(role);
        sb.append(", List<Roles>=").append(roles);
        sb.append("]");
        return sb.toString();
    }
}