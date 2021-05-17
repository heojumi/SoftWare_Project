package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


import android.os.Bundle;
import android.widget.TextView;

public class WritePost extends AppCompatActivity {

    private final int GET_IMG_FROM_GALLERY=200;
    private final int LOCATION_REQUEST=100;
    private ImageView imageview;
    private EditText editText;

    TextView locationTextView;
    LocationManager manager;
    GPSListener gpsListener;
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
    
        
        //본문 (content) 내용 string으로 바꿔오기
        editText=(EditText) findViewById(R.id.content);
        String mainText=editText.getText().toString();

        //버튼 누르면 현재 위치
        Button locationbutton=findViewById(R.id.locationbutton);
        manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //gpsListener=new GPSListener();
        locationbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

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

abstract class GPSListener implements LocationListener{
    @Override
    public void onLocationChanged(Location location){
        double latitude=location.getLatitude();
        double longtitude=location.getLongitude();
        String locMsg="위도: "+latitude+" 경도: "+longtitude;
      //locationTextView.setText(locMsg);
    }
}