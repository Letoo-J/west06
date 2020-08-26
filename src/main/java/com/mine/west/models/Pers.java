package com.mine.west.models;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class Pers implements Serializable {
    private Integer pID;

    private String persName;

    private String url;

    private static final long serialVersionUID = 1L;

    public Pers(Integer pID, String persName, String url) {
        this.pID = pID;
        this.persName = persName;
        this.url = url;
    }

    public Pers() {
        super();
    }

    public Integer getpID() {
        return pID;
    }

    public void setpID(Integer pID) {
        this.pID = pID;
    }

    public String getPersName() {
        return persName;
    }

    public void setPersName(String persName) {
        this.persName = persName == null ? null : persName.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pID=").append(pID);
        sb.append(", persName=").append(persName);
        sb.append(", url=").append(url);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}