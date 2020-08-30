package com.mine.west.models;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
public class Picture {
    private String pictureName;
    private Integer pbID;
    private Integer blogID;

    public Picture() {
    }

    public Picture(String pictureName, Integer pbID, Integer blogID) {
        this.blogID = blogID;
        this.pbID = pbID;
        this.pictureName = pictureName;
    }

    public Picture(Integer pbID, Integer blogID, String pictureName) {
        this.blogID = blogID;
        this.pbID = pbID;
        this.pictureName = pictureName;
    }

    public void setBlogID(Integer blogID) {
        this.blogID = blogID;
    }

    public void setPbID(Integer pbID) {
        this.pbID = pbID;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public Integer getBlogID() {
        return blogID;
    }

    public Integer getPbID() {
        return pbID;
    }

    public String getPictureName() {
        return pictureName;
    }

}
