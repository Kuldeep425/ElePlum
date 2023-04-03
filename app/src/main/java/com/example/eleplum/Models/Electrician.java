package com.example.eleplum.Models;

import java.io.Serializable;

public class Electrician implements Serializable {
    private String name;
    private String phone;
    private String electricianId;



    public Electrician (String name, String phone) {
        this.name = name;
        this.phone = phone;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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


}
