package com.mine.west.models;

import java.io.Serializable;

public class AccountRole implements Serializable {
    private Integer arID;

    private Integer accountID;

    private Integer roleID;

    private static final long serialVersionUID = 1L;

    public AccountRole(Integer arID, Integer accountID, Integer roleID) {
        this.arID = arID;
        this.accountID = accountID;
        this.roleID = roleID;
    }

    public AccountRole() {
        super();
    }

    public Integer getArID() {
        return arID;
    }

    public void setArID(Integer arID) {
        this.arID = arID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", arID=").append(arID);
        sb.append(", accountID=").append(accountID);
        sb.append(", roleID=").append(roleID);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}