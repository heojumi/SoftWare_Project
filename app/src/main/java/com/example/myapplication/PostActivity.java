package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostActivity extends Fragment {

    ItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_post, container, false);
        View root=inflater.inflate(R.layout.activity_post,container,false);
        Button btn=root.findViewById(R.id.writeButton);
        Button showcon=root.findViewById(R.id.ContentButton);

        init(root);
        getData();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),WritePost.class);
                startActivity(intent);
            }
        });
        showcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte=new Intent(getActivity(),PostContent.class);
                startActivity(inte);
            }
        });
        return root;

    }
    void init(View view) {
        //RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);
    }

    void getData() {

        List<String> listTitle=new ArrayList<>();
        List<String> listAddress=new ArrayList<>();
        List<String> listDate=new ArrayList<>();
        List<Bitmap> listImg=new ArrayList<>();
        String sql="select * from Post";
        Cursor cursor=MainActivity.db.rawQuery(sql,null);
        String tit;
        double lat,longi;
        byte[] bit;
        String datetime;
        while(cursor.moveToNext()){
            tit=cursor.getString(1);
            lat=cursor.getDouble(3);
            longi=cursor.getDouble(4);
            bit=cursor.getBlob(5);
            datetime=cursor.getString(6);

            Log.v("check","dattime: "+datetime);

            String add=getCurrentAddress(lat,longi);
            Bitmap bitmap= BitmapFactory.decodeByteArray(bit,0,bit.length);
            String date=datetime.substring(0,11);
            Log.v("check","date: "+date);

            listTitle.add(" "+tit);
            listAddress.add(add);
            listImg.add(bitmap);
            listDate.add(" "+date);
        }
        /*
        listTitle= Arrays.asList("상도동 앞 고양이 실종", "새끼 강아지 임보해주실분");
        listAddress=Arrays.asList(
                "서울시 동작구 상도동",
                "서울시 동작구 상도동"
        );
        List<String> listDate=Arrays.asList(
                "2021-04-11",
                "2021-05-31"
        );
        List<Integer> listImage=Arrays.asList(
                R.drawable.list,
                R.drawable.list
        );
         */
        for(int i=0;i<listTitle.size();i++) {
            ItemContent data=new ItemContent(listTitle.get(i),listAddress.get(i),listDate.get(i),listImg.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }

    public String getCurrentAddress(double latitude,double longitude){
        Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
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
        String adr=address.getAddressLine(0).toString().replaceFirst("대한민국","");
        //return address.getAdminArea()+" "+address.getSubLocality()+" "+address.getThoroughfare().toString();  //지오코더 주소 자르기, 순서대로 도/행정구역/동 단위
        return adr;

        //return address.getAddressLine(0).toString()+"\n";
    }


}