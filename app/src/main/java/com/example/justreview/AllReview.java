package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
            review.id = cursor.getInt(0);
            listReview.add(review);
        }
        //xep lai theo thu tu moi nhat
        Collections.reverse(listReview);

        AllReviewAdapter adapter = new AllReviewAdapter(listReview, getApplicationContext());
        lvReview.setAdapter(adapter);

        lvReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CommentDetails.class);
                Review review = listReview.get(i);

                intent.putExtra("ID", review.id);
                startActivity(intent);
            }
        });
    }
}