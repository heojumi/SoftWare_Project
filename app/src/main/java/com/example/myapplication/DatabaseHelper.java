package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android. database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;



public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG, "DataBaseHelper 생성자 호출");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Table Create");
        String createQuery = "CREATE TABLE " + "User" +
                "( UID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ID TEXT NOT NULL, " +
                "PW TEXT);";

        sqLiteDatabase.execSQL(createQuery);
        Log.v("check","exec success");

         String createQuery2 = "CREATE TABLE " + "Post" +
                "( PID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "contents TEXT," +
                "latitude REAL," +
                "longtitude REAL," +
                "image BLOB," +
                "created_day DATETIME DEFAULT CURRENT_TIMESTAMP);";

        sqLiteDatabase.execSQL(createQuery2);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }





}


