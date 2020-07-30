package com.mine.west.service;

import com.mine.west.exception.ModelException;
import com.mine.west.models.Followlist;

import java.util.List;

public interface FollowService {

    /**
     * 查询关注人数
     *
     * @param accountID
     * @return
     */
    int readFollowNumber(Integer accountID) throws ModelException;

    /**
     * 查询粉丝人数
     *
     * @param accountID
     * @return
     */
    int readFanNumber(Integer accountID) throws ModelException;

    /**
     * 查询关注用户
     *
     * @param accountID
     * @return
     */
    List<Followlist> readFollowAccount(Integer accountID) throws ModelException;

    /**
     * 查询粉丝用户
     *
     * @param accountID
     * @return
     */
    List<Followlist> readFanAccount(Integer accountID) throws ModelException;

    /**
     * 增加关注
     *
     * @param followlist
     * @return fID
     */
    int create(Followlist followlist) throws ModelException;

    /**
     * 取消关注
     *
     * @param fID
     * @return
     */
    boolean cancelFollow(Integer fID) throws ModelException;

}
