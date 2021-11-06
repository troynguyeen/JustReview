package com.example.justreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, ReviewAdapter.OnReviewListener, Serializable {
    SmoothBottomBar smoothBottomBar;
    AppCompatImageView menuIcon;
    ViewPager2  reviewViewPager;
    String dbName = "JustReviewDatabase.db";
    String DB_Path =  "/databases/";
    ReviewAdapter reviewAdapter;
    List<Review> reviewList;
    public SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Chi xai cai nay khi ma database bi mat hoac khong co
        //Cho no chay 1 lan thoi roi comment lai

        // CopyDatabaseFromFolderAsset();
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        reviewViewPager = findViewById(R.id.reviewViewPager);
        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        setupReviewViewPager();




        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        menuIcon = findViewById(R.id.menuIcon);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);

            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        smoothBottomBar = (SmoothBottomBar) findViewById(R.id.smoothBottomBar);
        smoothBottomBar.setItemActiveIndex(0);

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
    }

    private void CopyDatabaseFromFolderAsset() {
        File dbfile = getDatabasePath(dbName);
        CopyDatabase();
        if(!dbfile.exists()){
            CopyDatabase();
        }else{
            dbfile.delete();
            CopyDatabase();
        }

    }
    private void CopyDatabase() {

        try {

            InputStream myInput = getAssets().open(dbName);
            String outFileName = getApplicationInfo().dataDir+DB_Path+dbName;
            File f = new File(getApplicationInfo().dataDir+DB_Path);

            if(!f.exists()){
                f.mkdir();

            }
            OutputStream myOutPut = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int len;

            while ((len = myInput.read(buffer)) > 0){
                myOutPut.write(buffer, 0, len);

            }

            myOutPut.flush();
            myInput.close();
            myOutPut.close();

        }catch(Exception e){
            e.printStackTrace();
            Log.e("Loi sao chep",e.toString());
        }
    }
    public void goToFavouritePage(){

    }
    private void setupReviewViewPager() {
        reviewViewPager.setClipToPadding(false);
        reviewViewPager.setClipChildren(false);
        reviewViewPager.setOffscreenPageLimit(4);
        reviewViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        }));
        reviewViewPager.setPageTransformer(compositePageTransformer);

        reviewViewPager.setAdapter(new ReviewAdapter(getReview(), this));


    }


    private List<Review> getReview() {
        reviewList = new ArrayList<>();


        Cursor cursor = database.query("DanhSachReview", null, null, null, null, null, null);
        reviewList.clear();

        while (cursor.moveToNext()){
           Review book = new Review();
           book.image = cursor.getBlob(3);
           book.name = cursor.getString(1);
           book.author = cursor.getString(5);
           book.postDate = "12/10/2021";
           book.rating = cursor.getFloat(4);
           book.description = cursor.getString(2);
           book.id = cursor.getInt(0);
           book.theloai = cursor.getInt(6);
           reviewList.add(book);
        }

        return reviewList;
    }

    public void switchPage(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.sideMenuHome:
                switchPage(new MainActivity());
                break;
            case R.id.sideMenuShare:
                // do you click actions for the second selection
                break;
            case R.id.sideMenuSetting:
                // do you click actions for the third selection
                break;
            case R.id.sideMenuAddReview:
                switchPage(new AddBook());
                break;
        }

        return true;
    }

    @Override
    public void onReviewClick(int position) {
        Intent intent = new Intent(this, CommentDetails.class);
        Review review = reviewList.get(position);
        intent.putExtra("ID", review.id);
        startActivity(intent);
    }
}