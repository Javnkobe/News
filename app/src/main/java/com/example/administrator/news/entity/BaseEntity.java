package com.example.administrator.news.entity;

public class BaseEntity<T> {

    private String message;//返回文字内容
    private String status;//状态
    private T data;//数据(可能是集合也可能是对象)

    public BaseEntity() {
        // TODO Auto-generated constructor stub
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
