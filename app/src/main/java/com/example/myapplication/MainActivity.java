package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(getApplicationContext(),WritePost.class);
                startActivityForResult(intent,REQUEST_CODE_MENU);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==REQUEST_CODE_MENU){
            Toast.makeText(getApplicationContext(),"toast code: "+requestCode+"result: "+requestCode,Toast.LENGTH_LONG).show();

            if(resultCode==RESULT_OK){
                String name=data.getStringExtra("name");
                Toast.makeText(getApplicationContext(),"name: "+name,Toast.LENGTH_LONG).show();
            }
        }
    }
}

//채완 성공~~
//소민
//윤영
//주미
//예린

//주석추가