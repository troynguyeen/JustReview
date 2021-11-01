package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AddBook extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    Spinner spDanhmuc;
    MainActivity mainActivity;
    AppCompatImageView menuIcon;
    Button buttonAdd;
    String dbName = "JustReviewDatabase.db";
    EditText txtTitle, txtAuthor, txtNoiDung;
    public SQLiteDatabase database = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerAddLayout);

        menuIcon = findViewById(R.id.menuIcon);
        buttonAdd = findViewById(R.id.buttonAdd);

        txtTitle = findViewById(R.id.txtTitle);
        txtAuthor = findViewById(R.id.txtAuthorName);
        txtNoiDung = findViewById(R.id.txtNoiDung);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);

            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        spDanhmuc=(Spinner) findViewById(R.id.spDanhMuc);
        ArrayList<String> danhmuc =new ArrayList<String>();

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        Cursor cursor = database.query("DanhMuc", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            danhmuc.add(cursor.getString(1));
        }

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,danhmuc);

        spDanhmuc.setAdapter(arrayAdapter);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtTitle.getText().toString().trim().length()!=0 && txtAuthor.getText().toString().trim().length()!=0 && txtNoiDung.getText().toString().trim().length()!=0){
                    ContentValues values = new ContentValues();
                    values.put("TieuDe", txtTitle.getText().toString().trim());
                    values.put("NoiDung", txtNoiDung.getText().toString().trim());
                    values.put("TacGia", txtAuthor.getText().toString().trim());

                    spDanhmuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            values.put("IDDanhMuc", position);
                            //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    database.insert("DanhSachReview",null, values );

                    Toast.makeText(getApplicationContext(), "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                    txtTitle.setText("");
                    txtAuthor.setText("");
                    txtNoiDung.setText("");
                }else{
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ các trường thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
}