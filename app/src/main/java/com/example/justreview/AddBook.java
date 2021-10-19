package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddBook extends AppCompatActivity {
    Spinner spDanhmuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        spDanhmuc=(Spinner) findViewById(R.id.spDanhMuc);
        ArrayList<String> danhmuc=new ArrayList<String>();
        danhmuc.add("Sách IT");
        danhmuc.add("Sách Tiếng Anh");
        danhmuc.add("Sách Tin Học");
        danhmuc.add("Sách Kinh Tế");
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,danhmuc);

        spDanhmuc.setAdapter(arrayAdapter);
    }
}