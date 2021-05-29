package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PostContent extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

        TextView title=(TextView)findViewById(R.id.showTitle);
        TextView location=(TextView)findViewById(R.id.showLocationText);
        TextView content=(TextView)findViewById(R.id.showContent);
        ImageView img=(ImageView)findViewById(R.id.showImage);
        double lat=0;
        double longi=0;
        byte[] bit=null;
        String tit=null;
        String con=null;


        DatabaseHelper databaseHelper = new DatabaseHelper(PostContent.this, "DB", null, 1);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        
        //PostActivity에서 사용자가 클릭한 게시글 있으면, 그거 intent로 전달 -> intent받아서 pid든 뭐든 으로 구별짓기
        String sql="select * from Post where PID=2";
        Cursor cursor=db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            tit=cursor.getString(1);
            con=cursor.getString(2);
            lat=cursor.getDouble(3);
            longi=cursor.getDouble(4);
            bit=cursor.getBlob(5);
        }

        title.setText(tit);
        content.setText(con);
        location.setText(showAddressString(lat,longi));
        Bitmap bitmap= BitmapFactory.decodeByteArray(bit,0,bit.length);
        img.setImageBitmap(bitmap);
        //img.setImageURI();


    }

    public String showAddressString(double latitude,double longitude){
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        List<Address> add;
        try{
            add=geocoder.getFromLocation(latitude,longitude,10);

        } catch (IOException ioException) {
            ioException.printStackTrace();
            //Toast.makeText(this,"지오코더 주소 변환 불가",Toast.LENGTH_LONG).show();
            return "지오코더 주소 변환 불가";
        }catch (IllegalArgumentException illegalArgumentException){
            //Toast.makeText(this,"잘못된 GPS 좌표",Toast.LENGTH_LONG).show();
            return "잘못된  GPS 좌표";
        }

        if(add==null||add.size()==0){
            return "주소 미발견";
        }
        Address address=add.get(0);
        //return address.getAdminArea()+" "+address.getSubLocality()+" "+address.getThoroughfare().toString();  //지오코더 주소 자르기, 순서대로 도/행정구역/동 단위

        return address.getAddressLine(0).toString();
    }
}
