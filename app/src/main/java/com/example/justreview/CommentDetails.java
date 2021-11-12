package com.example.justreview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CommentDetails extends AppCompatActivity {
    ListView lvComment;
    ArrayList<Comment> comments;
    String dbName = "JustReviewDatabase.db";
    TextView reviewNameV, descriptionV, authorNameV, theLoaiV;
    ImageView photo;
    Review review;
    Button returnButton, deleteButton, updateButton;
    public SQLiteDatabase database = null;
    private SharedPreferenceConfig sharedPreferenceConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);
        reviewNameV = (TextView)findViewById(R.id.book_name);
        descriptionV = (TextView)findViewById(R.id.book_description);
        lvComment = (ListView) findViewById(R.id.lvComment);
        authorNameV = (TextView) findViewById(R.id.author_name);
        photo = (ImageView) findViewById(R.id.photoDetail);
        theLoaiV = (TextView)findViewById(R.id.the_loai);
        returnButton = (Button)findViewById(R.id.ReturnButton);
        deleteButton = (Button)findViewById(R.id.buttonDelete);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        if(sharedPreferenceConfig.read_admin_status() == false){
            deleteButton.setVisibility(View.INVISIBLE);
            updateButton.setVisibility(View.INVISIBLE);
        }else{
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        review = new Review();

        Bundle extras = getIntent().getExtras();
        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);
        Cursor cursor = database.query("DanhSachReview", null, null, null, null, null, null);

        while (cursor.moveToNext()){
            if(cursor.getInt(0) == extras.getInt("ID")){
                review.image = cursor.getBlob(3);
                review.name = cursor.getString(1);
                review.author = cursor.getString(5);
                review.rating = cursor.getFloat(4);
                review.description = cursor.getString(2);
                review.theloai = cursor.getInt(6);
                review.id = cursor.getInt(0);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Xóa review");
        builder.setMessage("Bạn có muốn xóa bài review này?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                database.delete("DanhSachReview", "ID=?", new String[]{Integer.toString(review.id)});
                Toast.makeText(getApplicationContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);


            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });
        //Toast.makeText(getApplicationContext(),Integer.toString(review.theloai),Toast.LENGTH_SHORT).show();
        Cursor cursor2 = database.query("DanhMuc", null, null, null, null, null, null);
        while (cursor2.moveToNext()){
            if(cursor2.getInt(0) == review.theloai + 1 ){
                theLoaiV.setText(cursor2.getString(1));
            }

        }


        if(review.image != null){
            byte[] bookImage = review.image;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bookImage, 0, bookImage.length);
            photo.setImageBitmap(bitmap);
        }else{
            photo.setImageResource(0);
        }

        reviewNameV.setText(review.name);
        descriptionV.setText(review.description);
        authorNameV.setText(review.author);


        comments = new ArrayList<Comment>();
        Comment comment = new Comment("Cong","Bai viet rat hay",R.drawable.usericon );
        comments.add(comment);

        comment = new Comment("Dat","Bai viet rat hay",R.drawable.usericon);
        comments.add(comment);

        comment = new Comment("Cong","Bai viet rat hay", R.drawable.usericon);
        comments.add(comment);

        adapter_comment listAdapterComment = new adapter_comment(comments, getApplicationContext());
        lvComment.setAdapter(listAdapterComment);

    }


    public void edid(View view) {
        Intent intent = new Intent(this, Edit.class);

        intent.putExtra("ID", review.id);

        startActivity(intent);
    }
}