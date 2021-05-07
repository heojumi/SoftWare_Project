package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class WritePost extends AppCompatActivity {

    private final int GET_IMG_FROM_GALLERY=200;
    private ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        imageview=(ImageView)findViewById(R.id.imageView);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_IMG_FROM_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_IMG_FROM_GALLERY&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){

            Uri selectedImageUri=data.getData();
            //url db에 저장
            imageview.setImageURI(selectedImageUri);

            
        }
    }

}