package com.example.justreview;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class UserLoginActivity extends FragmentActivity {

    EditText username, password;
    Button btnlogin, btnNextRegister;
    String DB = "JustReviewDatabase.db";

    public SQLiteDatabase database = null;


    private SharedPreferenceConfig sharedPreferenceConfig;

//    SmoothBottomBar smoothBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.userName1);
        password = (EditText) findViewById(R.id.password1);
        btnNextRegister = (Button) findViewById(R.id.btnNextRegister);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        database = openOrCreateDatabase(DB,MODE_PRIVATE,null);

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("")||pass.equals(""))
                    Toast.makeText(UserLoginActivity.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = checkusernamepassword(user, pass);
                    if (checkuserpass==true) {
                        Toast.makeText(UserLoginActivity.this,"Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        sharedPreferenceConfig.login_status(true);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(UserLoginActivity.this,"Nhập sai Tài khoản hoặc Mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnNextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

//        smoothBottomBar = (SmoothBottomBar) findViewById(R.id.smoothBottomBar);
//        smoothBottomBar.setItemActiveIndex(3);
//
//        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public boolean onItemSelect(int i) {
//                switch (i) {
//                    case 0:
//                        switchPage(new MainActivity());
//                        break;
//                    case 1:
//                        switchPage(new FavoriteActivity());
//                        break;
//                    case 2:
//                        switchPage(new CategoryActivity());
//                        break;
//                    case 3:
//                        switchPage(new UserLoginActivity());
//                        break;
//                    default:
//                        switchPage(new MainActivity());
//                        break;
//                }
//                smoothBottomBar.setItemActiveIndex(i);
//                return true;
//            }
//        });
    }

    public Boolean checkusernamepassword(String username, String password){
        Cursor cursor = database.rawQuery("Select * from TaiKhoanUser where TenTK = ? and MatKhau = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public void switchPage(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
    }
}
