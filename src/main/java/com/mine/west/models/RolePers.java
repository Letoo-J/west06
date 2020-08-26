package com.mine.west.models;

import java.io.Serializable;

public class RolePers implements Serializable {
    private Integer rpID;

    private Integer roleID;

    private Integer persID;

    private static final long serialVersionUID = 1L;

    public RolePers(Integer rpID, Integer roleID, Integer persID) {
        this.rpID = rpID;
        this.roleID = roleID;
        this.persID = persID;
    }

    public RolePers() {
        super();
    }

    public Integer getRpID() {
        return rpID;
    }

    public void setRpID(Integer rpID) {
        this.rpID = rpID;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }

    public Integer getPersID() {
        return persID;
    }

    public void setPersID(Integer persID) {
        this.persID = persID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", rpID=").append(rpID);
        sb.append(", roleID=").append(roleID);
        sb.append(", persID=").append(persID);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}