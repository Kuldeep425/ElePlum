package com.example.eleplum.Models;

public class InterestedEle {
    private String eleId;
    private String name;
    private String imageUrl;

    public InterestedEle(String eleId, String name, String imageUrl) {
        this.eleId = eleId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getEleId() {
        return eleId;
    }

    public void setEleId(String eleId) {
        this.eleId = eleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
