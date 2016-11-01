package com.example.administrator.news.entity;

/**
 * 有关注册和登录的 entity
 */
public class Register {

    String result; //服务器返回结果
    String token; //用户令牌，用于验证用户。每次请求都传递给服务器。具有时效期。
    String explain;//服务器返回结果说明

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
