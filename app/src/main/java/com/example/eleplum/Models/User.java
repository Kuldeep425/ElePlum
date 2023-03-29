package com.example.eleplum.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String phoneNumber;
    private String password;
    private String phonePassword;

    public User(String name, String phoneNumber, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    public User(String name, String phoneNumber, String password,String phonePassword) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.phonePassword=phonePassword;
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

    public String getPhonePassword() {
        return phonePassword;
    }

    public void setPhonePassword(String phonePassword) {
        this.phonePassword = phonePassword;
    }
}
