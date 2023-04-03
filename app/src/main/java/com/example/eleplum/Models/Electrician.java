package com.example.eleplum.Models;


import java.io.Serializable;


public class Electrician implements Serializable {
    private String electricianId;
    private String name;
    private String password;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private String imageURL;
    private String phone;
    private String phonePass;

    public Electrician(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    public Electrician(){
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setElectricianId(String electricianId) {
        this.electricianId = electricianId;
    }
    public String getElectricianId(){
        return electricianId;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonePass() {
        return phonePass;
    }

    public void setPhonePass(String phonePass) {
        this.phonePass = phonePass;
    }
}
