package com.tiodev.vegtummy.Model;

public class ResModel  {
    private String img, tittle, des, category, time, tag;

    public ResModel(String img, String tittle, String des, String category, String time, String tag) {
        this.img = img;
        this.tittle = tittle;
        this.des = des;
        this.category = category;
        this.time = time;
        this.tag = tag;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
