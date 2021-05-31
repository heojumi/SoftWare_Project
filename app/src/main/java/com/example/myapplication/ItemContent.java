package com.example.myapplication;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.Date;

public class ItemContent {

    Bitmap image;
    String title;
    String address;
    String date;
    int id;


    public ItemContent(int id,String title, String address, String date, Bitmap image) {
        this.image=image;
        this.title=title;
        this.address=address;
        this.date=date;
        this.id=id;
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

    public int getId(){return id;}
}
