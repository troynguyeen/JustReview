package com.example.justreview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.Collections;

public class ShowDm extends AppCompatActivity {
    AppCompatImageView searchBtn;
    EditText searchEdit;
    ListView lvReview;
    ArrayList<Review> listReview;
    String dbName = "JustReviewDatabase.db";
    String DB_Path =  "/databases/";
    public SQLiteDatabase database = null;

    public ShowDm() {
        listReview = new ArrayList<Review>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allreview);

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);

        lvReview = findViewById(R.id.LvReview);
        searchBtn = findViewById(R.id.searchBtn);
        searchEdit = findViewById(R.id.searchEdit);

        // Search review from list
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = searchEdit.getText().toString();
                ArrayList<Review> searchReviews = new ArrayList<Review>();
                for (Review review : listReview){
                    if(review.name.toLowerCase().contains(searchKey.toLowerCase().trim())) {
                        searchReviews.add(review);
                    }
                }
                fetchReviewList(searchReviews);
            }
        });
        Bundle extras = getIntent().getExtras();
        //Get review from db
        Cursor cursor = database.query("DanhSachReview", null, null, null, null, null, null);
        listReview.clear();


        while (cursor.moveToNext()){
            if (cursor.getInt(6) == extras.getInt("DM")) {
                Review review = new Review();
                review.image = cursor.getBlob(3);
                review.name = cursor.getString(1);
                review.id = cursor.getInt(0);
                listReview.add(review);
            }
        }
        fetchReviewList(listReview);

    }

    private void fetchReviewList(ArrayList<Review> list){
        //xep lai theo thu tu moi nhat
        Collections.reverse(list);

        AllReviewAdapter adapter = new AllReviewAdapter(list, getApplicationContext());
        lvReview.setAdapter(adapter);

        lvReview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CommentDetails.class);
                Review review = list.get(i);

                intent.putExtra("ID", review.id);
                startActivity(intent);
            }
        });
    }
}
