package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.sqlite.SQLiteDatabase;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements onTabItemSelectedListener {


    PostActivity fragment2;
    MyinfoActivity fragment3;
    BottomNavigationView bottomNavigation;
    static SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper databaseHelper = new DatabaseHelper(this, "DB", null, 1);

        db = databaseHelper.getWritableDatabase();



        fragment2 = new PostActivity();
        fragment3 = new MyinfoActivity();

        Intent intent=new Intent(this, MapActivity.class);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab1:
                                Toast.makeText(getApplicationContext(), "주변동물병원", Toast.LENGTH_LONG).show();
                                startActivityForResult(intent,1);
                                return true;

                            case R.id.tab2:
                                Toast.makeText(getApplicationContext(), "유기실종동물", Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                                return true;

                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "내정보", Toast.LENGTH_LONG).show();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
                                return true;
                        }
                        return false;
                    }
                });
        //db.close();
        //databaseHelper.close();

    }
    public void onTabSelected(int position) {
        if(position==0) {
            bottomNavigation.setSelectedItemId(R.id.tab1);
        } else if(position==1) {
            bottomNavigation.setSelectedItemId(R.id.tab2);
        } else if(position==2) {
            bottomNavigation.setSelectedItemId(R.id.tab3);
        }
    }

    public SQLiteDatabase sendDatabase(){
        return db;
    }


}