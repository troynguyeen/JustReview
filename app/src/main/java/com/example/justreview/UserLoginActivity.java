package com.example.justreview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class UserLoginActivity extends FragmentActivity {
    SmoothBottomBar smoothBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        smoothBottomBar = (SmoothBottomBar) findViewById(R.id.smoothBottomBar);
        smoothBottomBar.setItemActiveIndex(3);

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
    }

    public void switchPage(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
    }
}
