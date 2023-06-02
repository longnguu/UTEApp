package com.example.uteapp.Model;

import java.util.ArrayList;
import java.util.List;

public class PicVideos {
    List<String> loai = new ArrayList<>();
    List<String> link = new ArrayList<>();

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
