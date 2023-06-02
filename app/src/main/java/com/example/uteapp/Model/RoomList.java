package com.example.uteapp.Model;

public class RoomList {
    String tenNhom,soLuong,moTa,pass,chu,avt,key;

    public RoomList() {
        this.tenNhom = "";
        this.soLuong = "";
        this.moTa = "";
        this.pass="";
        this.chu="";
        this.key="";
        this.avt="https://global-uploads.webflow.com/5e157548d6f7910beea4e2d6/62dea764ea3e597ad347e785_group-logo-maker.png";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTenNhom() {
        return tenNhom;
    }

    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getChu() {
        return chu;
    }

    public void setChu(String chu) {
        this.chu = chu;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public RoomList(String tenNhom, String soLuong, String moTa, String pass, String chu, String avt) {
        this.tenNhom = tenNhom;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.pass = pass;
        this.chu = chu;
        this.avt = avt;
    }
}
