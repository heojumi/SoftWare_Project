package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Arrays;
import java.util.List;

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
        List<String> listTitle= Arrays.asList("상도동 앞 고양이 실종", "새끼 강아지 임보해주실분");
        List<String> listAddress=Arrays.asList(
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

        for(int i=0;i<listTitle.size();i++) {
            ItemContent data=new ItemContent(listTitle.get(i),listAddress.get(i),listDate.get(i),listImage.get(i));

            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }


}