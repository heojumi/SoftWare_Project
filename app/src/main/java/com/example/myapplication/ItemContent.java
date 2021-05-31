package com.example.myapplication;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.Date;

public class ItemContent { //set,get도 만들라는데......
    String id;
    Bitmap image;
    String title;
    String address;
    String date;


    public ItemContent(String title, String address, String date, Bitmap image) {
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

    public Bitmap getImage() {
        return image;
    }
}
