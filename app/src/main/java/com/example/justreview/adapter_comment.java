package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter_comment extends BaseAdapter {

    ArrayList<Comment> list;
    Context context;
    public adapter_comment(){

    }
    public adapter_comment(ArrayList<Comment> list,Context context){
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
        view = layoutInflater.inflate(R.layout.activity_adapter_comment,null);

        ImageView imgReview = (ImageView)view.findViewById(R.id.UserImage);
        imgReview.setImageResource(list.get(i).getImageId());

        TextView titleReview = (TextView)view.findViewById(R.id.userCmName);
        titleReview.setText(list.get(i).getName());

        TextView categoryReview = (TextView)view.findViewById(R.id.userComment);
        categoryReview.setText(list.get(i).getComment());

        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
        ratingBar.setRating(list.get(i).getScore());

        return view;
    }
}