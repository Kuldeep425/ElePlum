package com.example.eleplum.Models;

import java.util.List;

public class CreatedTask {
    private String taskId;
    private String userId;
    private double latitude;
    private double longitude;
    private String time;
    private String date;
    private String desc;
    private String address;
    private List<String>AcceptedUser;

    public CreatedTask(String taskId, String userId, double latitude, double longitude, String time, String date, String desc,String address) {
        this.taskId=taskId;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.date = date;
        this.desc = desc;
        this.address=address;
    }
    public  CreatedTask(){

    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getAcceptedUser() {
        return AcceptedUser;
    }

    public void setAcceptedUser(List<String> acceptedUser) {
        AcceptedUser = acceptedUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
