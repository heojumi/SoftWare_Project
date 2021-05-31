package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    ArrayList<ItemContent> listData=new ArrayList<ItemContent>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(ItemContent data) {
        listData.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView address;
        TextView date;
        ImageView image;

        ItemViewHolder(View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.item_title);
            address=itemView.findViewById(R.id.item_address);
            date=itemView.findViewById(R.id.item_date);
            image=itemView.findViewById(R.id.item_img);
        }

        void onBind(ItemContent data) {
            title.setText(data.getTitle());
            address.setText(data.getAddress());
            date.setText(data.getDate());
            image.setImageBitmap(data.getImage());
        }
    }
}
