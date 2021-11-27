package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class CategoryActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    SmoothBottomBar smoothBottomBar;
    Button btnIT,btnAV,btnVH;
    private SharedPreferenceConfig sharedPreferenceConfig;
    String dbName = "JustReviewDatabase.db";
    public SQLiteDatabase database = null;
    AppCompatImageView menuIcon;
    TextView userNameSideNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        menuIcon = findViewById(R.id.menuIcon);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
                userNameSideNavigation = findViewById(R.id.userNameTopBar);
                if(sharedPreferenceConfig.read_login_status() == false){
                    userNameSideNavigation.setText("Khách");
                    TextView role = findViewById(R.id.role);
                    role.setVisibility(View.INVISIBLE);
                    ConstraintLayout sidebar = (ConstraintLayout) findViewById(R.id.sidebar);
                    sidebar.setBackgroundResource(R.drawable.background_gradient_guest);

                }else{
                    if(sharedPreferenceConfig.read_admin_status() == true){
                        Cursor cursor = database.query("TaiKhoanAdmin", null, null, null, null, null, null);
                        while(cursor.moveToNext()){
                            if(cursor.getInt(0) == sharedPreferenceConfig.read_admin_id()){
                                userNameSideNavigation.setText(cursor.getString(3));
                            }
                        }
                    }
                    else{
                        Cursor cursor = database.query("TaiKhoanUser", null, null, null, null, null, null);
                        while(cursor.moveToNext()){
                            if(cursor.getInt(0) == sharedPreferenceConfig.read_user_id()){
                                userNameSideNavigation.setText(cursor.getString(3));
                            }
                        }
                        TextView role = findViewById(R.id.role);
                        role.setVisibility(View.INVISIBLE);
                        ConstraintLayout sidebar = (ConstraintLayout) findViewById(R.id.sidebar);
                        sidebar.setBackgroundResource(R.drawable.background_gradient_user);
                    }
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        if(sharedPreferenceConfig.read_login_status() == false){
            navigationView.getMenu().findItem(R.id.sideMenuLogout).setVisible(false);
            navigationView.getMenu().findItem(R.id.sideMenuAddReview).setVisible(false);
            navigationView.getMenu().findItem(R.id.sideMenuLogin).setVisible(true);
            navigationView.getMenu().findItem(R.id.sideMyFavourite).setVisible(false);
        }else{
            if(sharedPreferenceConfig.read_admin_status() == true){
                navigationView.getMenu().findItem(R.id.sideMyFavourite).setVisible(false);
                navigationView.getMenu().findItem(R.id.sideMenuAddReview).setVisible(true);
            }else{
                navigationView.getMenu().findItem(R.id.sideMenuAddReview).setVisible(false);
                navigationView.getMenu().findItem(R.id.sideMyFavourite).setVisible(true);
            }
            navigationView.getMenu().findItem(R.id.sideMenuLogout).setVisible(true);
            navigationView.getMenu().findItem(R.id.sideMenuLogin).setVisible(false);
        }





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
                        if(sharedPreferenceConfig.read_login_status() == false){
                            switchPage(new UserLoginActivity());
                            finish();
                        }else{
                            if(sharedPreferenceConfig.read_admin_status() == true){
                                switchPage(new AdminInformationActivity());
                                finish();
                            }else{
                                switchPage(new UserInformationActivity());
                                finish();
                            }
                        }

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
                Intent intent=new Intent(getApplicationContext(),ShowDm.class);
                intent.putExtra("DM",1);
                startActivity(intent);
                finish();
            }
        });
        btnAV=findViewById(R.id.btnAV);
        btnAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ShowDm.class);
                intent.putExtra("DM",2);
                startActivity(intent);
                finish();
            }
        });
        btnVH=findViewById(R.id.btnVH);
        btnVH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ShowDm.class);
                intent.putExtra("DM",3);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.sideMenuHome:
                switchPage(new MainActivity());
                finish();
                break;
            case R.id.sideMenuShare:
                // do you click actions for the second selection
                break;
            case R.id.sideMenuAllReview:
                switchPage(new AllReview());
                finish();
                break;
            case R.id.sideMenuSetting:
                // do you click actions for the third selection
                break;
            case R.id.sideMenuAddReview:
                switchPage(new AddBook());
                finish();
                break;
            case R.id.sideMenuLogout:
                sharedPreferenceConfig.login_status(false);
                sharedPreferenceConfig.is_admin_status(false);
                // 0 tức là đăng xuất
                sharedPreferenceConfig.set_user_id(0);
                sharedPreferenceConfig.set_admin_id(0);
                Toast.makeText(getApplicationContext(),"Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                switchPage(new MainActivity());
                finish();
                break;
            case R.id.sideMenuLogin:
                switchPage(new UserLoginActivity());
                finish();
                break;
            case R.id.sidedMenuCategory:
                switchPage(new CategoryActivity());
                finish();
                break;
            case R.id.sideMyFavourite:
                switchPage(new FavoriteActivity());
                finish();
                break;
        }


        return true;
    }
    public void switchPage(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
        finish();
    }
}