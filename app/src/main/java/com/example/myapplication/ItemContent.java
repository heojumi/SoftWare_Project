package com.example.myapplication;

import android.net.UrlQuerySanitizer;

import java.net.URL;
import java.sql.Date;

public class ItemContent { //set,get도 만들라는데......
    String id;
    URL picture;
    String title;
    String address;
    Date date;


    public ItemContent(String id, URL picture, String title, String address, Date date) {
        this.id=id;
        this.picture=picture;
        this.title=title;
        this.address=address;
        this.date=date;
    }
}
