package com.mine.west.models;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class Collect implements Serializable {
    private Integer cID;

    private Integer accountID;

    private Integer blogID;

    private static final long serialVersionUID = 1L;

    public Collect(Integer cID, Integer accountID, Integer blogID) {
        this.cID = cID;
        this.accountID = accountID;
        this.blogID = blogID;
    }

    public Collect() {
        super();
    }

    public Integer getcID() {
        return cID;
    }

    public void setcID(Integer cID) {
        this.cID = cID;
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
        sb.append(", eID=").append(cID);
        sb.append(", accountID=").append(accountID);
        sb.append(", blogID=").append(blogID);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
