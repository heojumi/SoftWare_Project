package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ActionBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
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
    static String loc="";
    List<Integer> listID=new ArrayList<>();;
    List<String> listTitle=new ArrayList<>();;
    List<String> listAddress=new ArrayList<>();;
    List<String> listDate=new ArrayList<>();;
    List<Bitmap> listImg=new ArrayList<>();;
    RecyclerView recyclerView;
    int count=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_post, container, false);
        View root=inflater.inflate(R.layout.activity_post,container,false);
        init(root);
        EditText location=(EditText)root.findViewById(R.id.editTextTextPersonName2);
        Button submit=(Button)root.findViewById(R.id.button3);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loc=location.getText().toString();
                Log.v("check","location: "+loc);
                listID.clear();
                listTitle.clear();
                listAddress.clear();
                listDate.clear();
                listImg.clear();
                adapter.clearItem();
                getData();
                recyclerView.invalidate();
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        listID.clear();
        listTitle.clear();
        listAddress.clear();
        listDate.clear();
        listImg.clear();
        adapter.clearItem();
        getData();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ItemAdapter.ItemViewHolder holder, View view, int position) {
                ItemContent item=adapter.getItem(position);
                //눌렀을때 코드!!
                //Toast.makeText(getActivity().getApplicationContext(),"클릭성공, position: "+position,Toast.LENGTH_LONG).show();
                int pid=item.getId();
                Intent tocontent=new Intent(getActivity(),PostContent.class);
                tocontent.putExtra("pid",pid);
                startActivity(tocontent);
            }
        });
        return root;

    }//end of oncreate view

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_write,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_write:
                Intent intent=new Intent(getActivity(),WritePost.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void init(View view) {
        //RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);
    }

    void getData() {

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext(), "DB", null, 1);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String sql="select * from Post";
        Cursor cursor=db.rawQuery(sql,null);
        int con_id;
        String tit;
        double lat,longi;
        byte[] bit;
        String datetime;
        while(cursor.moveToNext()){
            con_id=cursor.getInt(0);
            tit=cursor.getString(1);
            lat=cursor.getDouble(3);
            longi=cursor.getDouble(4);
            bit=cursor.getBlob(5);
            datetime=cursor.getString(6);

            String add=getCurrentAddress(lat,longi,1);
            Bitmap bitmap= BitmapFactory.decodeByteArray(bit,0,bit.length);
            String date=datetime.substring(0,11);

            if(loc.equals("")) {
                listID.add(con_id);
                listTitle.add(" " + tit);
                listAddress.add(add);
                listImg.add(bitmap);
                listDate.add(" " + date);
            }
            else{
                String subarea=getCurrentAddress(lat,longi,2);
                if(loc.equals(subarea)){
                    listID.add(con_id);
                    listTitle.add(" " + tit);
                    listAddress.add(add);
                    listImg.add(bitmap);
                    listDate.add(" " + date);
                }
            }
        }
        Log.v("check","size: "+listTitle.size());
        for(int i=0;i<listTitle.size();i++) {
            ItemContent data=new ItemContent(listID.get(i),listTitle.get(i),listAddress.get(i),listDate.get(i),listImg.get(i));
            Log.v("check","new item: "+listID.get(i));
            adapter.addItem(data);
        }
        recyclerView.invalidate();
        //adapter.notifyDataSetChanged();
        recyclerView.getAdapter().notifyDataSetChanged();
    }//end of getdata

    public String getCurrentAddress(double latitude,double longitude,int i){
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
        if(i==1) {
            String adr = address.getAddressLine(0).toString().replaceFirst("대한민국", "");
            //return address.getAdminArea()+" "+address.getSubLocality()+" "+address.getThoroughfare().toString();  //지오코더 주소 자르기, 순서대로 도/행정구역/동 단위
            return adr;
        }
        else{
            String adr=address.getSubLocality();
            return adr;
        }

        //return address.getAddressLine(0).toString()+"\n";
    }


}