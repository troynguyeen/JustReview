package com.example.justreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;

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
    ConstraintLayout reviewRecommended;
    RoundedImageView imageRecommended;
    TextView nameRecommended;
    TextView authorRecommended;
    TextView categoryRecommended;
    TextView ratingCount;
    RatingBar ratingRecommended;
    private int idRecommended = 0;
    EditText search;
    AppCompatImageView searchBtn;
    SmoothBottomBar smoothBottomBar;
    AppCompatImageView menuIcon;
    ViewPager2  reviewViewPager;
    String dbName = "JustReviewDatabase.db";
    String DB_Path =  "/databases/";
    ReviewAdapter reviewAdapter;
    List<Review> reviewList;
    public SQLiteDatabase database = null;
    TextView textViewAll;
    private SharedPreferenceConfig sharedPreferenceConfig;

    TextView userNameSideNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Click on Review recommended
        reviewRecommended = findViewById(R.id.reviewRecommended);
        reviewRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idRecommended != 0) {
                    Intent intent = new Intent(MainActivity.this, CommentDetails.class);
                    intent.putExtra("ID", idRecommended);
                    startActivity(intent);
                }
            }
        });

        // Search review
        search = findViewById(R.id.search);
        searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = search.getText().toString();
                ArrayList<Review> searchReviews = new ArrayList<Review>();

                for (Review review : reviewList) {
                    if(review.name.toLowerCase().contains(searchKey.toLowerCase().trim())) {
                        searchReviews.add(review);
                    }
                }

                setupReviewViewPager(searchReviews);
            }
        });

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        textViewAll = (TextView) findViewById(R.id.textViewAll);

        textViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchPage(new AllReview());
            }

        });



    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //Chi xai cai nay khi ma database bi mat hoac khong co
        //Cho no chay 1 lan thoi roi comment lai

    //CopyDatabaseFromFolderAsset();
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~s~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        reviewViewPager = findViewById(R.id.reviewViewPager);
        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        getReview();
        setupReviewViewPager(reviewList);
        fetchReviewRecommended();

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        menuIcon = findViewById(R.id.menuIcon);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
                userNameSideNavigation = findViewById(R.id.userNameTopBar);
                if(sharedPreferenceConfig.read_login_status() == false){
                    userNameSideNavigation.setText("Khách");
                }else{
                    if(sharedPreferenceConfig.read_admin_status() == true){
                        userNameSideNavigation.setText("Quản trị viên");
                    }else{
                        Cursor cursor = database.query("TaiKhoanUser", null, null, null, null, null, null);
                        while(cursor.moveToNext()){
                            if(cursor.getInt(0) == sharedPreferenceConfig.read_user_id()){
                                userNameSideNavigation.setText(cursor.getString(3));
                            }
                        }
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
        smoothBottomBar.setItemActiveIndex(0);

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
                            switchPage(new LoginAdminActivity());

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
    private void setupReviewViewPager(List<Review> reviewList) {
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

        reviewViewPager.setAdapter(new ReviewAdapter(reviewList, this));
    }

    private List<Review> getReview() {
        reviewList = new ArrayList<Review>();

        Cursor cursor = database.query("DanhSachReview", null, null, null, null, null, null);
        reviewList.clear();

        while (cursor.moveToNext()){
           Review book = new Review();
           book.image = cursor.getBlob(3);
           book.name = cursor.getString(1);
           book.author = cursor.getString(5);
           book.rating = cursor.getFloat(4);
           book.description = cursor.getString(2);
           book.id = cursor.getInt(0);

           // set rating count
            Cursor commentCursor = database.rawQuery("SELECT CountTable.IDDanhSachReview as id, CountTable.ratingCount, CountTable.totalRating FROM (SELECT IDDanhSachReview, COUNT(IDDanhSachReview) as ratingCount, SUM(DiemDanhGia) as totalRating FROM BinhLuan GROUP BY IDDanhSachReview) CountTable WHERE id = " + cursor.getInt(0),null);
            while(commentCursor.moveToNext()) {
                book.ratingCount = commentCursor.getInt(1);
            }

            // set category
            Cursor categoryCursor = database.rawQuery("SELECT * FROM DanhMuc", null);
            while(categoryCursor.moveToNext()) {
                if(categoryCursor.getInt(0) == cursor.getInt(6)){
                    book.theLoaiText = categoryCursor.getString(1);
                }
            }

           reviewList.add(book);
        }

        return reviewList;
    }

    private void fetchReviewRecommended() {
        imageRecommended = findViewById(R.id.imageRecommended);
        nameRecommended = findViewById(R.id.nameRecommended);
        authorRecommended = findViewById(R.id.authorRecommended);
        categoryRecommended = findViewById(R.id.categoryRecommended);
        ratingCount = findViewById(R.id.ratingCount);
        ratingRecommended = findViewById(R.id.ratingRecommended);

        int idMaxRate = 0;

        Cursor commentCursor = database.rawQuery("SELECT CountTable.IDDanhSachReview, MAX(CountTable.ratingCount), CountTable.totalRating FROM (SELECT IDDanhSachReview, COUNT(IDDanhSachReview) as ratingCount, SUM(DiemDanhGia) as totalRating FROM BinhLuan GROUP BY IDDanhSachReview) CountTable",null);

        while(commentCursor.moveToNext()) {
            idMaxRate = commentCursor.getInt(0);
            ratingCount.setText("(" + commentCursor.getInt(1) + " Đánh giá)");
        }

        Cursor recommendedCursor = database.rawQuery("SELECT * FROM DanhSachReview WHERE ID = " + idMaxRate, null);

        while(recommendedCursor.moveToNext()) {
            idRecommended = recommendedCursor.getInt(0);
            nameRecommended.setText(recommendedCursor.getString(1));
            authorRecommended.setText(recommendedCursor.getString(5));
            ratingRecommended.setRating(recommendedCursor.getFloat(4));

            // set image recommended
            if(recommendedCursor.getBlob(3) != null){
                byte[] bookImage = recommendedCursor.getBlob(3);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bookImage, 0, bookImage.length);
                imageRecommended.setImageBitmap(bitmap);
            }else{
                imageRecommended.setImageResource(0);
            }

            // set category recommended
            Cursor categoryCursor = database.rawQuery("SELECT * FROM DanhMuc", null);
            while(categoryCursor.moveToNext()) {
                if(categoryCursor.getInt(0) == recommendedCursor.getInt(6)){
                    categoryRecommended.setText(categoryCursor.getString(1));
                }
            }
        }
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

    @Override
    public void onReviewClick(int position) {

        Intent intent = new Intent(this, CommentDetails.class);
        Review review = reviewList.get(position);
        intent.putExtra("ID", review.id);
        startActivity(intent);
    }
}