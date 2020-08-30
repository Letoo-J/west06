package com.mine.west.dao;

import com.mine.west.models.Picture;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PictureMapper {

    int insertPicture(Picture picture);

    List<Picture> readByBlogID(Integer blogID);

}
