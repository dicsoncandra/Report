package com.example.lazykid.report;

/**
 * Created by Lazykid on 12/7/17.
 */

public class Blog {
    private String title, desc, image, name, date, type;

    public Blog() {

    }

    public Blog(String title, String desc, String name, String date, String type, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.name = name;
        this.date = date;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}