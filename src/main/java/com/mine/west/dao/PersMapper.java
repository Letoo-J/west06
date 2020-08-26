package com.mine.west.dao;

import com.mine.west.models.Pers;
import java.util.List;

public interface PersMapper {
    /**
     * 根据角色id查询权限集合
     * @param id
     * @return
     */
    List<Pers> findPermsByRoleId(Integer id);

    int deleteByPrimaryKey(Integer pID);

    int insert(Pers record);

    Pers selectByPrimaryKey(Integer pID);

    List<Pers> selectAll();

    int updateByPrimaryKey(Pers record);
}