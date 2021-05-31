package com.example.myapplication;

import java.net.URL;

public class ItemContent { //set,get도 만들라는데......
    String id;
    int image;
    String title;
    String address;
    String date;


    public ItemContent(String title, String address, String date, int image) {
        this.image=image;
        this.title=title;
        this.address=address;
        this.date=date;
    }



    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public int getImage() {
        return image;
    }
}
