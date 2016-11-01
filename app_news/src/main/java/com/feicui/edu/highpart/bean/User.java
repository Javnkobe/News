package com.feicui.edu.highpart.bean;

import java.util.List;

public class User {
    private String uid;//用户 id
    private String email;//用户邮箱
    private int integration;//用户积分
    private int comnum;//评论数量
    private String portrait;//头像

    public List<LoginLog> getLoginlog() {
        return loginlog;
    }

    public void setLoginlog(List<LoginLog> loginlog) {
        this.loginlog = loginlog;
    }

    private List<LoginLog> loginlog;// 登陆日志

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public int getComnum() {
        return comnum;
    }

    public void setComnum(int comnum) {
        this.comnum = comnum;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

}
