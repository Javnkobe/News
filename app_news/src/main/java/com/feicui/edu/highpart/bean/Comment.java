package com.feicui.edu.highpart.bean;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class Comment {
    private int cid;//“cid”:评论编号
    private String uid;//“uid”:评论者名字
    private String portrait;//“portrait”:用户头像链接
    private String stamp;//“stamp”:评论时间

    public Comment() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    private String content;//“content":评论内容

    public Comment(int cid, String uid, String portrait, String stamp, String content) {
        this.cid = cid;
        this.uid = uid;
        this.portrait = portrait;
        this.stamp = stamp;
        this.content = content;
    }
}
