package com.mine.west.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlogAll implements Serializable {
    @ApiModelProperty(value = "博客ID")
    private Integer blogID;         //博客ID

    @ApiModelProperty(value = "发布者ID")
    private Integer accountID;      //发布者ID

    /**
     * 使返回json数据时，时间以此格式输出:
     *
     * @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
     * 使返回json数据时，输出的key值为自定义的(对象属性名不变！):
     * @JsonProperty("releaseTime02") 使返回json数据时，若此属性为空，则不返回:
     * @JsonInclude(JsonInclude.Include.NON_NULL) 使返回json数据时，此标注的属性不返回给前端（敏感数据）,不进行请求响应的输出；
     * @JsonIgnore
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "发布时间")
    private Date releaseTime;       //发布时间

    @ApiModelProperty(value = "点赞数")
    private Integer likeNumber;     //点赞数

    @ApiModelProperty(value = "转发数")
    private Integer repostNumber;   //转发数

    @ApiModelProperty(value = "评论数")
    private Integer commentNumber;  //评论数

    @ApiModelProperty(value = "博客内容")
    private String content;         //博客内容

    @ApiModelProperty(value = "用户名")
    private String name;

    private static final long serialVersionUID = 1L;

    public BlogAll(Blog blog, String name) {
        this.name = name;
        this.accountID = blog.getAccountID();
        this.blogID = blog.getBlogID();
        this.releaseTime = blog.getReleaseTime();
        this.commentNumber = blog.getCommentNumber();
        this.likeNumber = blog.getLikeNumber();
        this.repostNumber = blog.getRepostNumber();
        this.content = blog.getContent();
    }
}
