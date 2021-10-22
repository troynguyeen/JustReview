package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.ListView;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class FavoriteActivity extends AppCompatActivity {
    SmoothBottomBar smoothBottomBar;
    ListView lvFavorite;
    ArrayList<Favorite> fvr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        smoothBottomBar = (SmoothBottomBar) findViewById(R.id.smoothBottomBar);
        smoothBottomBar.setItemActiveIndex(1);

        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        switchPage(new MainActivity());
                        break;
                    case 1:
                        switchPage(new FavoriteActivity());
                        break;
                    case 2:
                        switchPage(new CategoryActivity());
                        break;
                    case 3:
                        switchPage(new UserLoginActivity());
                        break;
                    default:
                        switchPage(new MainActivity());
                        break;
                }
                return true;
            }
        });

        lvFavorite = (ListView) findViewById(R.id.lvFavorite);
        fvr = new ArrayList<Favorite>();
        Favorite db = new Favorite("Review CODE Dạo Ký Sự", "Sách IT", R.drawable.book_codedao);
        fvr.add(db);

        db = new Favorite("Review Hack Não 1500 từ", "Sách Tiếng Anh", R.drawable.book_hacknao1500tuvung);
        fvr.add(db);

        db = new Favorite("Review Hoàng Tử Bé", "Sách Nổi Bật", R.drawable.book_hoang_tu_be);
        fvr.add(db);

        ListAdapterFavorite listAdapterFavorite = new ListAdapterFavorite(fvr, getApplicationContext());
        lvFavorite.setAdapter(listAdapterFavorite);
    }

    public void switchPage(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
    }
}