package com.example.uteapp.Model;

public class CommentList {
    String keyy,name,cmt,date,time,avt;
    String dataKey=null;

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public CommentList(String keyy, String name, String cmt, String date, String time, String avt) {
        this.keyy = keyy;
        this.name = name;
        this.cmt = cmt;
        this.date = date;
        this.time = time;
        this.avt = avt;
    }
    public CommentList() {
        this.keyy = "";
        this.name = "";
        this.cmt = "";
        this.date = "";
        this.time = "";
        this.avt = "";
    }

    public String getKeyy() {
        return keyy;
    }

    public void setKeyy(String keyy) {
        this.keyy = keyy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}
