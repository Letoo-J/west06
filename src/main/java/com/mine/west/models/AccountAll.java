package com.mine.west.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountAll implements Serializable {
    @ApiModelProperty(value = "账户ID")
    private Integer accountID;

    @ApiModelProperty(value = "用户名（唯一）")
    private String name;

    @ApiModelProperty(value = "邮箱")
    private String mailbox;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "电话")
    private String mobilePhone;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "头像文件名")
    private String avatar;

    @ApiModelProperty(value = "个签签名")
    private String individualitySignature;

    @ApiModelProperty(value = "封禁状态")
    private String identity;

    @ApiModelProperty(value = "角色（无用的属性，不要用）")
    private String role;

    private Integer blogNum;

    private Integer followNum;

    private Integer fanNum;

    private static final long serialVersionUID = 1L;

    //定义角色集合
    private List<Roles> roles;

    public AccountAll(Account account, int blogNum, int followNum, int fanNum) {
        this.accountID = account.getAccountID();
        this.name = account.getName();
        this.mailbox = account.getMailbox();
        this.nickname = account.getNickname();
        this.mobilePhone = account.getMobilePhone();
        this.sex = account.getSex();
        this.avatar = account.getAvatar();
        this.individualitySignature = account.getIndividualitySignature();
        this.identity = account.getIdentity();
        this.role = account.getRole();
        this.blogNum = blogNum;
        this.followNum = followNum;
        this.fanNum = fanNum;
        this.roles = account.getRoles();
    }
}
