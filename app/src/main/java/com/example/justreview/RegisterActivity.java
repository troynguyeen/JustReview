package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity
{
    EditText username, password, repassword, realname;
    Button signup, btnDate;
    RadioGroup radioGrp;
    RadioButton radioButton;

    String DB = "JustReviewDatabase.db";

    public SQLiteDatabase database = null;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.confirmPassword);
        realname = (EditText) findViewById(R.id.realUserName);
        signup = (Button) findViewById(R.id.btnRegister);
        TextView signin =findViewById(R.id.btnNextLogin);
        btnDate = (Button) findViewById(R.id.datePickerButton);

        radioGrp = (RadioGroup) findViewById(R.id.radioGrp);


        database = openOrCreateDatabase(DB,MODE_PRIVATE,null);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String name = realname.getText().toString();
                String date = btnDate.getText().toString();


                int selectedId = radioGrp.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                String sex = radioButton.getText().toString();
                if (user.equals("")||pass.equals("")||repass.equals("")||name.equals("")||date.equals("")||sex.equals(""))
                    Toast.makeText(RegisterActivity.this,"Vui l??ng nh???p ?????y ????? th??ng tin",Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = checkusername(user);
                        if (checkuser == false) {
                            Boolean insert = insertData(user, pass, repass, name, date, sex);
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "????ng k?? th??nh c??ng",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this,"????ng k?? th???t b???i", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this,"T??n t??i kho???n ???? t???n t???i", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this,"M???t kh???u kh??ng ph?? h???p", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public Boolean insertData(String username, String password, String repassword, String name, String date, String sex){
        ContentValues contentValues= new ContentValues();
        contentValues.put("TenTK", username);
        contentValues.put("MatKhau", password);
        contentValues.put("NhapLaiMatKhau", repassword);
        contentValues.put("TenUser", name);
        contentValues.put("NgaySinh", date);
        contentValues.put("GioiTinh", sex);
        long result = database.insert("TaiKhoanUser", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkusername(String username) {
        Cursor cursor = database.rawQuery("Select * from TaiKhoanUser where TenTK = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public void checkButton(View v){
        int selectedId = radioGrp.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
    }
}