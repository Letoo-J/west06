package com.mine.west.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

/**
 * 指定json的k-v对顺序：
 *    @JsonPropertyOrder(value = {"content","releaseTime"})
 */
@Builder
@ApiModel(value = "Blog对象结构体")
public class Blog implements Serializable {
    @ApiModelProperty(value="博客ID")
    private Integer blogID;         //博客ID

    @ApiModelProperty(value="发布者ID")
    private Integer accountID;      //发布者ID

    /**
     * 使返回json数据时，时间以此格式输出:
     *     @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
     * 使返回json数据时，输出的key值为自定义的(对象属性名不变！):
     *     @JsonProperty("releaseTime02")
     * 使返回json数据时，若此属性为空，则不返回:
     *     @JsonInclude(JsonInclude.Include.NON_NULL)
     * 使返回json数据时，此标注的属性不返回给前端（敏感数据）,不进行请求响应的输出；
     *     @JsonIgnore
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value="发布时间")
    private Date releaseTime;       //发布时间

    @ApiModelProperty(value="点赞数")
    private Integer likeNumber;     //点赞数

    @ApiModelProperty(value="转发数")
    private Integer repostNumber;   //转发数

    @ApiModelProperty(value="评论数")
    private Integer commentNumber;  //评论数

    @ApiModelProperty(value="博客内容")
    private String content;         //博客内容

    private static final long serialVersionUID = 1L;

    //测试
    public Blog(Integer blogID) {
        this.blogID = blogID;
    }

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
        //sb.append("Hash = ").append(hashCode());
        sb.append("  blogID=").append(blogID);
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