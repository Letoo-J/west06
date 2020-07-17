package com.mine.west.models;

import java.io.Serializable;

public class Relay implements Serializable {
    private Integer rID;

    private Integer accountID;

    private Integer blogID;

    private static final long serialVersionUID = 1L;

    public Relay(Integer rID, Integer accountID, Integer blogID) {
        this.rID = rID;
        this.accountID = accountID;
        this.blogID = blogID;
    }

    public Relay() {
        super();
    }

    public Integer getrID() {
        return rID;
    }

    public void setrID(Integer rID) {
        this.rID = rID;
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
        sb.append(", rID=").append(rID);
        sb.append(", accountID=").append(accountID);
        sb.append(", blogID=").append(blogID);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}