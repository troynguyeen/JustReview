package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserInformationActivity extends AppCompatActivity {
    TextView userName;
    EditText nameEditProfile, genderEditProfile, dobEditProfile, passwordEditProfile;
    Button saveProfile, cancelProfile;
    SharedPreferenceConfig share;
    private String dbName = "JustReviewDatabase.db";
    public SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        share = new SharedPreferenceConfig(getApplicationContext());

        userName = findViewById(R.id.userName);
        nameEditProfile = findViewById(R.id.nameEditProfile);
        genderEditProfile = findViewById(R.id.genderEditProfile);
        dobEditProfile = findViewById(R.id.dobEditProfile);
        passwordEditProfile = findViewById(R.id.passwordEditProfile);
        saveProfile = findViewById(R.id.saveProfile);
        cancelProfile = findViewById(R.id.cancelProfile);

        fetchProfile();

        // set calendar UI
        dobEditProfile.setInputType(InputType.TYPE_NULL);
        dobEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        dobEditProfile.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new DatePickerDialog(UserInformationActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        cancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInformationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchProfile() {
        int idUser = share.read_user_id();

        Cursor profileCursor = database.rawQuery("SELECT * FROM TaiKhoanUser WHERE ID = " + idUser, null);
        while (profileCursor.moveToNext()) {
            userName.setText(profileCursor.getString(1));
            nameEditProfile.setText(profileCursor.getString(3));
            dobEditProfile.setText(profileCursor.getString(4));
            genderEditProfile.setText(profileCursor.getString(5));
            passwordEditProfile.setText(profileCursor.getString(2));
        }
    }

    private void editProfile() {
        int idUser = share.read_user_id();

        if(nameEditProfile.getText().length() > 0
                && genderEditProfile.getText().length() > 0
                && dobEditProfile.getText().length() > 0
                && passwordEditProfile.getText().length() > 0) {

            ContentValues values = new ContentValues();
            values.put("TenUSer", nameEditProfile.getText().toString());
            values.put("GioiTinh", genderEditProfile.getText().toString());
            values.put("NgaySinh", dobEditProfile.getText().toString());
            values.put("MatKhau", passwordEditProfile.getText().toString());

            database.update("TaiKhoanUser", values, "ID = " + idUser, null);

            Toast.makeText(this, "Cập nhật thông tin User thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserInformationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }

    }
}