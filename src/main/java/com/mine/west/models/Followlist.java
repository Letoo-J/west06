package com.mine.west.models;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class Followlist implements Serializable {
    private Integer fID;

    private Integer followID;

    private Integer fanID;

    private static final long serialVersionUID = 1L;

    public Followlist(Integer fID, Integer followID, Integer fanID) {
        this.fID = fID;
        this.followID = followID;
        this.fanID = fanID;
    }

    public Followlist() {
        super();
    }

    public Integer getfID() {
        return fID;
    }

    public void setfID(Integer fID) {
        this.fID = fID;
    }

    public Integer getFollowID() {
        return followID;
    }

    public void setFollowID(Integer followID) {
        this.followID = followID;
    }

    public Integer getFanID() {
        return fanID;
    }

    public void setFanID(Integer fanID) {
        this.fanID = fanID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fID=").append(fID);
        sb.append(", followID=").append(followID);
        sb.append(", fanID=").append(fanID);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}