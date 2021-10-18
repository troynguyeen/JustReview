package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SachAdapter extends BaseAdapter {

    ArrayList<Sach> list;
    Context context;
    public SachAdapter(){

    }
    public SachAdapter(ArrayList<Sach> list,Context context){
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
        LayoutInflater layoutInflater=LayoutInflater.from(context.getApplicationContext());
        view=layoutInflater.inflate(R.layout.activity_sach_adapter,null);
        ImageView img = (ImageView)view.findViewById(R.id.anh);
        TextView txtsach = (TextView)view.findViewById(R.id.txtsach);
        txtsach.setText("[Góc Review]"+" "+list.get(i).getTenSach());
        img.setImageResource(list.get(i).getAnh());
        return view;
    }
}