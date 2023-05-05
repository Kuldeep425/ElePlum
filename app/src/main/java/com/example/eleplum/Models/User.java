package com.example.eleplum.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String name;
    private String phoneNumber;
    private String password;
    private String phonePass;
    private String fcmToken;


    public User(String name, String phoneNumber, String password,String phonePass) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.phonePass=phonePass;
        this.fcmToken=null;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonePass() {
        return phonePass;
    }

    public void setPhonePass(String phonePass) {
        this.phonePass = phonePass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
