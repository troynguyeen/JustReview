package com.example.justreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Edit extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    Spinner spDanhmuc;
    MainActivity mainActivity;
    AppCompatImageView menuIcon;
    Button buttonEdit;
    String dbName = "JustReviewDatabase.db";
    EditText txtTitle, txtAuthor, txtNoiDung;
    ImageView bookImg;
    Review review;
    private final int REQUEST_CODE_GALLERY = 999;

    public SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        menuIcon = findViewById(R.id.menuIcon);
        buttonEdit = findViewById(R.id.buttonEdit);
        spDanhmuc=(Spinner) findViewById(R.id.spDanhMuc);
        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthorName);
        txtNoiDung = findViewById(R.id.txtNoiDung);

        bookImg = findViewById(R.id.bookImg);

        loadDanhmuc();

        loadData();
        loadSpinner();

        bookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(Edit.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });

    }



    private void loadData() {
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

        spDanhmuc.setSelection(review.theloai - 1);
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
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtTitle.getText().toString().trim().length()!=0 && txtAuthor.getText().toString().trim().length()!=0 && txtNoiDung.getText().toString().trim().length()!=0){
                    ContentValues values = new ContentValues();
                    values.put("TieuDe", txtTitle.getText().toString().trim());
                    values.put("NoiDung", txtNoiDung.getText().toString().trim());
                    values.put("TacGia", txtAuthor.getText().toString().trim());
                    values.put("AnhSach",imageViewToByte(bookImg));

                    int idDanhMuc = spDanhmuc.getSelectedItemPosition() + 1;
                    values.put("IDDanhMuc", idDanhMuc);
                    //Toast.makeText(getApplicationContext(), String.valueOf(idDanhMuc), Toast.LENGTH_SHORT).show();


                    database.update("DanhSachReview",values,"ID=?",new String[]{Integer.toString(review.id)});

                    Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ các trường thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void loadSpinner() {

        ArrayList<String> danhmuc =new ArrayList<String>();
        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        Cursor cursor2 = database.rawQuery("Select * from DanhMuc where ID=?",new String[]{Integer.toString(review.id)});
        while (cursor2.moveToNext()) {
            if (cursor2.getInt(0) == review.theloai) {
                danhmuc.add(cursor2.getString(1));
                ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,danhmuc);

                spDanhmuc.setAdapter(arrayAdapter);
            }else {

            }
        }

    }

    private void loadDanhmuc() {
        spDanhmuc=(Spinner) findViewById(R.id.spDanhMuc);
        ArrayList<String> danhmuc =new ArrayList<String>();

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        Cursor cursor1 = database.query("DanhMuc", null, null, null, null, null, null);
        while (cursor1.moveToNext()){
            danhmuc.add(cursor1.getString(1));
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,danhmuc);

        spDanhmuc.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);

        switch (menuItem.getItemId()) {
            case R.id.sideMenuHome:
                mainActivity.switchPage(new MainActivity());
                break;
            case R.id.sideMenuShare:
                // do you click actions for the second selection
                break;
            case R.id.sideMenuSetting:
                // do you click actions for the third selection
                break;
            case R.id.sideMenuAddReview:
                mainActivity.switchPage(new AddBook());
                break;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bookImg.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}