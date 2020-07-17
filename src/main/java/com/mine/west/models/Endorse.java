package com.mine.west.models;

import java.io.Serializable;

public class Endorse implements Serializable {
    private Integer eID;

    private Integer accountID;

    private Integer blogID;

    private static final long serialVersionUID = 1L;

    public Endorse(Integer eID, Integer accountID, Integer blogID) {
        this.eID = eID;
        this.accountID = accountID;
        this.blogID = blogID;
    }

    public Endorse() {
        super();
    }

    public Integer geteID() {
        return eID;
    }

    public void seteID(Integer eID) {
        this.eID = eID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getBlogID() {
        return blogID;
    }

    public void setBlogID(Integer blogID) {
        this.blogID = blogID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", eID=").append(eID);
        sb.append(", accountID=").append(accountID);
        sb.append(", blogID=").append(blogID);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}