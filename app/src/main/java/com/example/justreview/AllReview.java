package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.sql.Blob;
import java.util.ArrayList;

public class AllReview extends AppCompatActivity {
    ListView lvReview;
    ArrayList<Review> listReview;
    String dbName = "JustReviewDatabase.db";
    String DB_Path =  "/databases/";
    public SQLiteDatabase database = null;

    public AllReview() {
        listReview = new ArrayList<Review>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allreview);

        lvReview = findViewById(R.id.LvReview);

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);

        //Get review from db
        Cursor cursor = database.query("DanhSachReview", null, null, null, null, null, null);
        listReview.clear();

        while (cursor.moveToNext()){
            Review review = new Review();
            review.image = cursor.getBlob(3);
            review.name = cursor.getString(1);
            listReview.add(review);
        }

        AllReviewAdapter adapter = new AllReviewAdapter(listReview, getApplicationContext());
        lvReview.setAdapter(adapter);
    }
}