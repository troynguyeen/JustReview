package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllReviewAdapter extends BaseAdapter {
    ArrayList<Review> listReview;
    Context context;

    public AllReviewAdapter(ArrayList<Review> _listReview,Context _context){
        listReview = _listReview;
        context = _context;
    }

    @Override
    public int getCount() {
        return listReview.size();
    }

    @Override
    public Object getItem(int i) {
        return listReview.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater=LayoutInflater.from(context.getApplicationContext());
        view = layoutInflater.inflate(R.layout.activity_sach_adapter,null);

        ImageView img = view.findViewById(R.id.anh);
        TextView txtsach = view.findViewById(R.id.txtsach);

        if(listReview.get(i).image != null){
            byte[] bookImage = listReview.get(i).image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bookImage, 0, bookImage.length);
            img.setImageBitmap(bitmap);
        }else{
            img.setImageResource(0);
        }

        txtsach.setText("[Góc Review]" + " " + listReview.get(i).name);

        return view;
    }
}