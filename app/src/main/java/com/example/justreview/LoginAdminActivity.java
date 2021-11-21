package com.example.justreview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class LoginAdminActivity extends FragmentActivity {
    EditText adminname, adminpassword;
    Button btnloginadmin;
    ImageView backButton;

    String DB = "JustReviewDatabase.db";

    public SQLiteDatabase database = null;


    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminname = (EditText) findViewById(R.id.adminName);
        adminpassword = (EditText) findViewById(R.id.adminPassword);
        btnloginadmin = (Button) findViewById(R.id.btnLoginAdmin);
        backButton = (ImageView) findViewById(R.id.backButton);
        database = openOrCreateDatabase(DB,MODE_PRIVATE,null);

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        btnloginadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String admin = adminname.getText().toString();
                String adminpass = adminpassword.getText().toString();

                if (admin.equals("")||adminpass.equals(""))
                    Toast.makeText(LoginAdminActivity.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkadminpass = checkadminnamepassword(admin, adminpass);
                    if (checkadminpass==true) {
                        Toast.makeText(LoginAdminActivity.this,"Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        sharedPreferenceConfig.login_status(true);
                        sharedPreferenceConfig.is_admin_status(true);
                        int adminID = returnAdminID(admin,adminpass);
                        sharedPreferenceConfig.set_admin_id(adminID);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginAdminActivity.this,"Nhập sai Tài khoản hoặc Mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    int returnAdminID(String adminname, String adminpassword){
        Cursor cursor = database.query("TaiKhoanAdmin", null, null, null, null, null, null);
        int id = 0;
        while (cursor.moveToNext()){
            if(cursor.getString(1).equals(adminname) && cursor.getString(2).equals(adminpassword)){
                id = cursor.getInt(0);
            }
        }
        return id;
    }
    public Boolean checkadminnamepassword(String adminname, String adminpassword){
        Cursor cursor = database.rawQuery("Select * from TaiKhoanAdmin where TenTK = ? and MatKhau = ?", new String[] {adminname,adminpassword});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
