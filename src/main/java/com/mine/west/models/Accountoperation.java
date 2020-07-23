package com.mine.west.models;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class Accountoperation implements Serializable {
    private Integer aID;

    private Integer accountID;

    private Integer blogID;

    private Float interest;

    private static final long serialVersionUID = 1L;

    public Accountoperation(Integer aID, Integer accountID, Integer blogID, Float interest) {
        this.aID = aID;
        this.accountID = accountID;
        this.blogID = blogID;
        this.interest = interest;
    }

    public Accountoperation() {
        super();
    }

    public Integer getaID() {
        return aID;
    }

    public void setaID(Integer aID) {
        this.aID = aID;
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

    public Float getInterest() {
        return interest;
    }

    public void setInterest(Float interest) {
        this.interest = interest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", aID=").append(aID);
        sb.append(", accountID=").append(accountID);
        sb.append(", blogID=").append(blogID);
        sb.append(", interest=").append(interest);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}