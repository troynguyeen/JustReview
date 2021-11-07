package com.example.justreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Edit extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener  {
    Review review;
    Spinner spDanhmuc;
    MainActivity mainActivity;
    AppCompatImageView menuIcon;
    Button buttonEdit;
    String dbName = "JustReviewDatabase.db";
    EditText txtTitle, txtAuthor, txtNoiDung;
    ImageView bookImg;

    private final int REQUEST_CODE_GALLERY = 999;

    public SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerAddLayout);
        menuIcon = findViewById(R.id.menuIcon);
        buttonEdit = findViewById(R.id.buttonEdit);

        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthorName);
        txtNoiDung = findViewById(R.id.txtNoiDung);
        spDanhmuc=findViewById(R.id.spDanhMuc);
        bookImg = findViewById(R.id.bookImg);
        review = new Review();

        Bundle extras = getIntent().getExtras();
        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        Cursor cursor = database.query("DanhSachReview", null, null, null, null, null, null);

        while (cursor.moveToNext()){
            if(cursor.getInt(0) == extras.getInt("ID")){
                review.image = cursor.getBlob(3);
                review.name = cursor.getString(1);
                review.author = cursor.getString(5);
                review.rating = cursor.getFloat(4);
                review.description = cursor.getString(2);
                review.theloai = cursor.getInt(6);
                review.id = cursor.getInt(0);
            }
        }
        txtTitle.setText(review.name);
        txtAuthor.setText(review.author);
        txtNoiDung.setText(review.description);
        if(review.image != null){
            byte[] bookImage = review.image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bookImage, 0, bookImage.length);
            bookImg.setImageBitmap(bitmap);
        }else{
            bookImg.setImageResource(0);
        }
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}