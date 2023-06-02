package com.example.uteapp.Model;

public class UserListRoom {
    String name,avt,id;

    public UserListRoom() {
        this.name="";
        this.avt="";
        this.id="";
    }

    public UserListRoom(String name, String avt, String id) {
        this.name = name;
        this.avt = avt;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}
