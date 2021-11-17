package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class CategoryActivity extends AppCompatActivity {
    SmoothBottomBar smoothBottomBar;
    Button btnIT,btnAV,btnVH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        smoothBottomBar = (SmoothBottomBar) findViewById(R.id.smoothBottomBar);
        smoothBottomBar.setItemActiveIndex(2);

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
                smoothBottomBar.setItemActiveIndex(i);
                return true;
            }
        });
        btnIT=findViewById(R.id.btnIT);
        btnIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ShowDM.class);
                intent.putExtra("dm",1);
                startActivity(intent);
            }
        });
        btnAV=findViewById(R.id.btnAV);
        btnAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ShowDM.class);
                intent.putExtra("dm",2);
                startActivity(intent);
            }
        });
        btnVH=findViewById(R.id.btnVH);
        btnVH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ShowDM.class);
                intent.putExtra("dm",3);
                startActivity(intent);
            }
        });
    }

    public void switchPage(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
    }
}