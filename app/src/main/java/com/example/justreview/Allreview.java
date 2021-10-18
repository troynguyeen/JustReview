package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Allreview extends AppCompatActivity {
    ListView lvsach;
    ArrayList<Sach> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allreview);

        lvsach = (ListView) findViewById(R.id.lvsach);
        a = new ArrayList<Sach>();
        Sach ds = new Sach(R.drawable.book_codedao, "sach hay");
        a.add(ds);

        ds = new Sach(R.drawable.book_hacknao1500tuvung, "sach hay");
        a.add(ds);

        ds = new Sach(R.drawable.book_hacknao_ielts, "sach hay");
        a.add(ds);

        SachAdapter adapter = new SachAdapter(a, getApplicationContext());
        lvsach.setAdapter(adapter);
    }
}