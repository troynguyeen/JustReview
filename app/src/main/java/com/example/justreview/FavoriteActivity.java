package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class FavoriteActivity extends AppCompatActivity {
    SmoothBottomBar smoothBottomBar;
    ListView lvFavorite;
    ArrayList<Review> fvr;
    ImageView favouriteLogo;
    TextView notification;
    ArrayList<Integer> danhsachReview;
    String dbName = "JustReviewDatabase.db";
    public SQLiteDatabase database = null;
    private SharedPreferenceConfig sharedPreferenceConfig;
    public FavoriteActivity() {
        fvr = new ArrayList<Review>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        notification = (TextView) findViewById(R.id.favouriteNotification);
        favouriteLogo = findViewById(R.id.favouriteLogo);
        lvFavorite = (ListView) findViewById(R.id.LvReview2);
        database = openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        smoothBottomBar = (SmoothBottomBar) findViewById(R.id.smoothBottomBar);
        smoothBottomBar.setItemActiveIndex(1);

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
                            switchPage(new UserLoginActivity());
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


        if (sharedPreferenceConfig.read_user_id() != 0) {
            danhsachReview = new ArrayList<Integer>();

            Cursor cursor = database.rawQuery("SELECT * FROM DanhSachYeuThich WHERE IDUser = " + sharedPreferenceConfig.read_user_id(), null);
            while (cursor.moveToNext()) {
                danhsachReview.add(cursor.getInt(2));
            }

            if(danhsachReview.size() == 0) {
                favouriteLogo.setImageResource(R.drawable.wishlist);
                favouriteLogo.getLayoutParams().width = 750;
                notification.setText("Bạn chưa có bài review yêu thích!");
            }
            else {
                notification.setVisibility(View.INVISIBLE);
                favouriteLogo.setVisibility(View.INVISIBLE);

                cursor = database.query("DanhSachReview", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    for (int i = 0; i < danhsachReview.size(); i++) {
                        if (cursor.getInt(0) == danhsachReview.get(i)) {
                            Review review = new Review();
                            review.image = cursor.getBlob(3);
                            review.name = cursor.getString(1);
                            review.id = cursor.getInt(0);
                            fvr.add(review);
                        }
                    }

                }

                AllReviewAdapter listAdapterFavorite = new AllReviewAdapter(fvr, getApplicationContext());
                lvFavorite.setAdapter(listAdapterFavorite);
            }
        }
        else if(sharedPreferenceConfig.read_admin_status()) {
            favouriteLogo.setImageResource(R.drawable.user_only);
            favouriteLogo.getLayoutParams().width = 1200;
            notification.setText("Chức năng chỉ dành cho User!");
        }

        lvFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CommentDetails.class);
                Review review = fvr.get(i);

                intent.putExtra("ID", review.id);
                startActivity(intent);
            }
        });


    }


    public void switchPage(Activity act) {
        Intent intent = new Intent(this, act.getClass());
        startActivity(intent);
    }
}