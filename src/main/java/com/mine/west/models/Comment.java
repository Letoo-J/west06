package com.mine.west.models;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private Integer commentID;

    private Integer blogID;

    private Integer accountID;

    private Integer likeNumber;

    private Integer receivedID;

    private Date commentTime;

    private String content;

    private static final long serialVersionUID = 1L;

    public Comment(Integer commentID, Integer blogID, Integer accountID, Integer likeNumber, Integer receivedID, Date commentTime, String content) {
        this.commentID = commentID;
        this.blogID = blogID;
        this.accountID = accountID;
        this.likeNumber = likeNumber;
        this.receivedID = receivedID;
        this.commentTime = commentTime;
        this.content = content;
    }

    public Comment() {
        super();
    }

    public Integer getCommentID() {
        return commentID;
    }

    public void setCommentID(Integer commentID) {
        this.commentID = commentID;
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

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Integer getReceivedID() {
        return receivedID;
    }

    public void setReceivedID(Integer receivedID) {
        this.receivedID = receivedID;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
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
        sb.append(", commentID=").append(commentID);
        sb.append(", blogID=").append(blogID);
        sb.append(", accountID=").append(accountID);
        sb.append(", likeNumber=").append(likeNumber);
        sb.append(", receivedID=").append(receivedID);
        sb.append(", commentTime=").append(commentTime);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}