package com.snail.clearvdsanalyze.entity;

import java.io.Serializable;

/**
 * @author yongjie created on 2019-08-04.
 */
public class ClearWebEntity implements Serializable {

    private String webUrl;
    private String vid;
    private int subVid;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getSubVid() {
        return subVid;
    }

    public void setSubVid(int subVid) {
        this.subVid = subVid;
    }

    @Override
    public String toString() {
        return "ClearWebEntity{" +
                "webUrl='" + webUrl + '\'' +
                ", vid='" + vid + '\'' +
                ", subVid='" + subVid + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
