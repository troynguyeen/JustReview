package com.example.justreview;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;

public class ShowDm extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    AppCompatImageView searchBtn;
    EditText searchEdit;
    ListView lvReview;
    ArrayList<Review> listReview;
    String dbName = "JustReviewDatabase.db";
    String DB_Path =  "/databases/";
    Review review;
    public SQLiteDatabase database = null;
    SharedPreferenceConfig sharedPreferenceConfig;
    AppCompatImageView menuIcon;
    TextView userNameSideNavigation;
    public ShowDm() {
        listReview = new ArrayList<Review>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allreview);

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);

        lvReview = findViewById(R.id.LvReview);
        searchBtn = findViewById(R.id.searchBtn);
        searchEdit = findViewById(R.id.searchEdit);

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



        // Search review from list
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = searchEdit.getText().toString();
                ArrayList<Review> searchReviews = new ArrayList<Review>();
                for (Review review : listReview){
                    if(review.name.toLowerCase().contains(searchKey.toLowerCase().trim())) {
                        searchReviews.add(review);
                    }
                }
                fetchReviewList(searchReviews);
            }
        });
        Bundle extras = getIntent().getExtras();
        //Get review from db
        Cursor cursor = database.query("DanhSachReview", null, null, null, null, null, null);
        listReview.clear();


        while (cursor.moveToNext()){
            if (cursor.getInt(6) == extras.getInt("DM")) {
                review = new Review();
                review.image = cursor.getBlob(3);
                review.name = cursor.getString(1);
                review.id = cursor.getInt(0);
                listReview.add(review);

            }
        }

        fetchReviewList(listReview);

    }

    private void fetchReviewList(ArrayList<Review> list){
        //xep lai theo thu tu moi nhat
        Collections.reverse(list);

        AllReviewAdapter adapter = new AllReviewAdapter(list, getApplicationContext());
        lvReview.setAdapter(adapter);

        lvReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CommentDetails.class);
                Review review = list.get(i);

                intent.putExtra("ID", review.id);
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
