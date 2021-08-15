package com.wwh.homework0801.model;

/**
 * @author Created by diandian
 * @date 2021/8/15.
 */
public class OrderInfo {
    private long id;
    private long userInfoId;

    public OrderInfo() {
    }

    public OrderInfo(long id, long userInfoId) {
        this.id = id;
        this.userInfoId = userInfoId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
    }
}
