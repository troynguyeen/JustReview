package com.example.justreview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapterFavorite extends BaseAdapter {

    ArrayList<Favorite> list;
    Context context;
    public ListAdapterFavorite(){

    }
    public ListAdapterFavorite(ArrayList<Favorite> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int i) {

        return list.get(i);
    }

    @Override
    public long getItemId(int i) {

        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context.getApplicationContext());
        view = layoutInflater.inflate(R.layout.activity_list_adapter_favorite,null);

        ImageView imgRiview = (ImageView)view.findViewById(R.id.imgCodeDao);
        imgRiview.setImageResource(list.get(i).getImageId());

//        ImageView imgTurned = (ImageView)view.findViewById(R.id.iconturned1);
//        imgTurned.setImageResource(list.get(i).getImageId());
//
//        ImageView imgShare = (ImageView)view.findViewById(R.id.iconshare1);
//        imgShare.setImageResource(list.get(i).getImageId());

        TextView titleReview = (TextView)view.findViewById(R.id.txtCodeDao);
        titleReview.setText(list.get(i).getTitle());

        TextView categoryReview = (TextView)view.findViewById(R.id.txtDanhmuc);
        categoryReview.setText(list.get(i).getCategory());

        TextView gachduoiReview = (TextView)view.findViewById(R.id.textView2);
        gachduoiReview.setText(list.get(i).getCategory());
        return view;
    }


}