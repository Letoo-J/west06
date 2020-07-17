package com.mine.west.models;

import java.io.Serializable;
import java.util.Date;

public class Blog implements Serializable {
    private Integer blogID;

    private Integer accountID;

    private Date releaseTime;

    private Integer likeNumber;

    private Integer repostNumber;

    private Integer commentNumber;

    private String content;

    private static final long serialVersionUID = 1L;

    public Blog(Integer blogID, Integer accountID, Date releaseTime, Integer likeNumber, Integer repostNumber, Integer commentNumber, String content) {
        this.blogID = blogID;
        this.accountID = accountID;
        this.releaseTime = releaseTime;
        this.likeNumber = likeNumber;
        this.repostNumber = repostNumber;
        this.commentNumber = commentNumber;
        this.content = content;
    }

    public Blog() {
        super();
    }

    public Integer getBlogID() {
        return blogID;
    }

    public void setBlogID(Integer blogID) {
        this.blogID = blogID;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Integer getRepostNumber() {
        return repostNumber;
    }

    public void setRepostNumber(Integer repostNumber) {
        this.repostNumber = repostNumber;
    }

    public Integer getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(Integer commentNumber) {
        this.commentNumber = commentNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", blogID=").append(blogID);
        sb.append(", accountID=").append(accountID);
        sb.append(", releaseTime=").append(releaseTime);
        sb.append(", likeNumber=").append(likeNumber);
        sb.append(", repostNumber=").append(repostNumber);
        sb.append(", commentNumber=").append(commentNumber);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}