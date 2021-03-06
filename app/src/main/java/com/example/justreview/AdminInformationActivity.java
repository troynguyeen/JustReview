package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminInformationActivity extends AppCompatActivity {
    RadioGroup genderEditProfile;
    RadioButton genderNam, genderNu, genderKhac;
    TextView userName;
    EditText nameEditProfile, dobEditProfile, passwordOldEditProfile, passwordNewEditProfile;
    Button saveProfile, cancelProfile;
    SharedPreferenceConfig share;
    private String dbName = "JustReviewDatabase.db";
    public SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_infomation);

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        share = new SharedPreferenceConfig(getApplicationContext());

        userName = findViewById(R.id.userName);
        nameEditProfile = findViewById(R.id.nameEditProfile);
        genderEditProfile = findViewById(R.id.genderEditProfile);
        genderNam = findViewById(R.id.genderNam);
        genderNu = findViewById(R.id.genderNu);
        genderKhac = findViewById(R.id.genderKhac);
        dobEditProfile = findViewById(R.id.dobEditProfile);
        passwordOldEditProfile = findViewById(R.id.passwordOldEditProfile);
        passwordNewEditProfile = findViewById(R.id.passwordNewEditProfile);
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
                new DatePickerDialog(AdminInformationActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                Intent intent = new Intent(AdminInformationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchProfile() {
        int idAdmin = share.read_admin_id();

        Cursor profileCursor = database.rawQuery("SELECT * FROM TaiKhoanAdmin WHERE ID = " + idAdmin, null);
        while (profileCursor.moveToNext()) {
            userName.setText(profileCursor.getString(1));
            nameEditProfile.setText(profileCursor.getString(3));
            dobEditProfile.setText(profileCursor.getString(4));

            switch (profileCursor.getString(5)) {
                case "Nam":
                    genderNam.setChecked(true);
                    break;
                case "N???":
                    genderNu.setChecked(true);
                    break;
                default:
                    genderKhac.setChecked(true);

            }
        }
    }

    private void editProfile() {
        int idAdmin = share.read_admin_id();

        if(nameEditProfile.getText().length() > 0
            && (int) genderEditProfile.getCheckedRadioButtonId() > 0
            && dobEditProfile.getText().length() > 0) {

            ContentValues values = new ContentValues();
            values.put("TenUser", nameEditProfile.getText().toString());
            values.put("NgaySinh", dobEditProfile.getText().toString());

            //Get selected radio button
            int genderId = genderEditProfile.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(genderId);
            values.put("GioiTinh", radioButton.getText().toString());

            // Get password old
            Cursor profileCursor = database.rawQuery("SELECT * FROM TaiKhoanAdmin WHERE ID = " + idAdmin, null);

            String passwordOld = "";
            while (profileCursor.moveToNext()) {
                passwordOld = profileCursor.getString(2);
            }

            if(passwordOldEditProfile.getText().toString().length() > 0 || passwordNewEditProfile.getText().toString().length() > 0){
                if(passwordNewEditProfile.getText().toString().length() == 0) {
                    Toast.makeText(this, "M???t kh???u m???i kh??ng ???????c ????? tr???ng!", Toast.LENGTH_SHORT).show();
                }
                else if(passwordOldEditProfile.getText().toString().equals(passwordNewEditProfile.getText().toString())) {
                    Toast.makeText(this, "M???t kh???u c?? v?? m???t kh???u m???i kh??ng ???????c tr??ng nhau!", Toast.LENGTH_SHORT).show();
                }
                else if(!passwordOldEditProfile.getText().toString().equals(passwordOld)) {
                    Toast.makeText(this, "M???t kh???u c?? kh??ng h???p l??? vui l??ng th??? l???i!", Toast.LENGTH_SHORT).show();
                }
                else {
                    values.put("MatKhau", passwordNewEditProfile.getText().toString());
                    database.update("TaiKhoanAdmin", values, "ID = " + idAdmin, null);
                    Toast.makeText(this, "C???p nh???t th??ng tin Admin th??nh c??ng!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminInformationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                database.update("TaiKhoanAdmin", values, "ID = " + idAdmin, null);
                Toast.makeText(this, "C???p nh???t th??ng tin Admin th??nh c??ng!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminInformationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            Toast.makeText(this, "Vui l??ng nh???p ?????y ????? th??ng tin!", Toast.LENGTH_SHORT).show();
        }

    }
}