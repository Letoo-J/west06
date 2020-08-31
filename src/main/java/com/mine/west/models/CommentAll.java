package com.mine.west.models;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentAll implements Serializable {
    private Integer commentID;

    private Integer blogID;

    private Integer accountID;

    private Integer likeNumber;

    private Integer receivedID;

    private Date commentTime;

    private String content;

    private String name;

    private static final long serialVersionUID = 1L;

    public CommentAll(Comment comment, String name) {
        this.name = name;
        this.commentID = comment.getCommentID();
        this.accountID = comment.getAccountID();
        this.blogID = comment.getBlogID();
        this.likeNumber = comment.getLikeNumber();
        this.receivedID = comment.getReceivedID();
        this.commentTime = comment.getCommentTime();
        this.content = comment.getContent();
    }
}
