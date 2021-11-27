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
import java.util.ArrayList;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class FavoriteActivity extends AppCompatActivity  implements
        NavigationView.OnNavigationItemSelectedListener{
    SmoothBottomBar smoothBottomBar;
    ListView lvFavorite;
    ArrayList<Review> fvr;
    ImageView favouriteLogo;
    TextView notification;
    ArrayList<Integer> danhsachReview;
    String dbName = "JustReviewDatabase.db";
    public SQLiteDatabase database = null;
    private SharedPreferenceConfig sharedPreferenceConfig;
    public FavoriteActivity() {
        fvr = new ArrayList<Review>();
    }
    AppCompatImageView menuIcon;
    TextView userNameSideNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        notification = (TextView) findViewById(R.id.favouriteNotification);
        favouriteLogo = findViewById(R.id.favouriteLogo);
        lvFavorite = (ListView) findViewById(R.id.LvReview2);
        database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());


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
        smoothBottomBar.setItemActiveIndex(1);

        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        switchPage(new MainActivity());
                        finish();
                        break;
                    case 1:
                        switchPage(new FavoriteActivity());
                        finish();
                        break;
                    case 2:
                        switchPage(new CategoryActivity());
                        finish();

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
                        finish();
                        break;
                }
                return true;
            }
        });


        if (sharedPreferenceConfig.read_user_id() != 0) {
            danhsachReview = new ArrayList<Integer>();

            Cursor cursor = database.rawQuery("SELECT * FROM DanhSachYeuThich WHERE IDUser = " + sharedPreferenceConfig.read_user_id(), null);
            while (cursor.moveToNext()) {
                danhsachReview.add(cursor.getInt(2));
            }

            if(danhsachReview.size() == 0) {
                favouriteLogo.setImageResource(R.drawable.wishlist);
                favouriteLogo.getLayoutParams().width = 750;
                notification.setText("Bạn chưa có bài review yêu thích!");
            }
            else {
                notification.setVisibility(View.INVISIBLE);
                favouriteLogo.setVisibility(View.INVISIBLE);

                cursor = database.query("DanhSachReview", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    for (int i = 0; i < danhsachReview.size(); i++) {
                        if (cursor.getInt(0) == danhsachReview.get(i)) {
                            Review review = new Review();
                            review.image = cursor.getBlob(3);
                            review.name = cursor.getString(1);
                            review.id = cursor.getInt(0);
                            fvr.add(review);
                        }
                    }

                }

                AllReviewAdapter listAdapterFavorite = new AllReviewAdapter(fvr, getApplicationContext());
                lvFavorite.setAdapter(listAdapterFavorite);
            }
        }
        else if(sharedPreferenceConfig.read_admin_status()) {
            favouriteLogo.setImageResource(R.drawable.user_only);
            favouriteLogo.getLayoutParams().width = 1200;
            notification.setText("Chức năng chỉ dành cho User!");
        }

        lvFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CommentDetails.class);
                Review review = fvr.get(i);

                intent.putExtra("ID", review.id);
                startActivity(intent);
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