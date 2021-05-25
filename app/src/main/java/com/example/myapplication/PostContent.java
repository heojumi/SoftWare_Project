package com.example.myapplication;

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

        title.setText("테스트: 여기는 제목칸 입니다!");
        location.setText(showAddressString(37.49461918939203, 126.95988886699955));
        content.setText("유리병 속에 담긴 나의 바다 파란 포도즙 밤하늘 저 푸른 달빛 부서져가는 나의 여름밤 파란 달나라로 나를 데려가줘요 어렸을적 파란밤 달빛 내리는 거릴 걷다가 소년을 바라보다 벼락맞아었지 그건 아마 어린 나에겐 사랑인줄도 모르고 가슴만 저려오며 파란달만 쳐다 보았네 밤이면 추억들이 파도에 밀려 바람에 실려 슬픈 지난일을 모두 데려가줘요 어렸을적 파란밤 달빛 내리는 거릴 걷다가 소년을 바라보다 벼락맞아었지 그건 아마 어린 나에겐 사랑인줄도 모르고 가슴만 저려오며 파란달만 쳐다 보았네 숨박꼭질 하던 소년 넌 어디로 숨어 버렸나 저기 저 파란 하늘만 조용히 웃고 있네요 어렸을적 파란밤 달빛 내리는 거릴 걷다가 소년을 바라보다 벼락맞아었지 그건 아마 어린 나에겐 사랑인줄도 모르고 가슴만 저려오며 파란달만 쳐다 보았네 그건 아마 어린 나에겐 사랑인줄도 모르고 가슴만 저려오며 파란달만 쳐다 보았네\n" );

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
