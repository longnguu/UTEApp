package com.example.uteapp.Model;

import java.util.ArrayList;
import java.util.List;

public class PicVideos {
    List<String> loai = new ArrayList<>();
    List<String> link = new ArrayList<>();
    List<String> des= new ArrayList<>(),title = new ArrayList<>();
    String avt,Key,ParentKey;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getParentKey() {
        return ParentKey;
    }

    public void setParentKey(String parentKey) {
        ParentKey = parentKey;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public PicVideos(List<String> loai, List<String> link, List<String> des, List<String> title) {
        this.loai = loai;
        this.link = link;
        this.des = des;
        this.title = title;
        avt="";
    }

    public List<String> getDes() {
        return des;
    }

    public void setDes(String des) {
            this.des.add(des);
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
            this.title.add(title);
    }

    public List<String> getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai.add(loai);
    }

    public List<String> getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link.add(link);
    }
    public int getSize(){
        return loai.size();
    }

    public PicVideos(List<String> loai, List<String> link) {
        this.loai = loai;
        this.link = link;
    }
    public PicVideos() {
    }
}
