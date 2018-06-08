package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/6/8.
 */

public class SearchUserCondition {
    private String userName;
    private int role;
    private int status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
