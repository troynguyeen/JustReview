package com.example.justreview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CommentDetails extends AppCompatActivity {
    ListView lvComment;
    ArrayList<Comment> comments;
    String dbName = "JustReviewDatabase.db";
    TextView reviewNameV, descriptionV, authorNameV, theLoaiV;
    EditText editComment;
    RatingBar ratingStar, totalRatingStar;
    ImageView photo, favouriteIcon, notFavouriteIcon, down_arrow;
    Review review;
    Button deleteButton, updateButton, submitComment, resetComment;
    LinearLayout layoutButtonComment;
    Animation from_bottom;
    ScrollView third_scrollview;
    public SQLiteDatabase database = null;
    private SharedPreferenceConfig sharedPreferenceConfig;
    Boolean alreadyHasFavourited = false;
    int selectedComment = -1;

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
        down_arrow = (ImageView) findViewById(R.id.down_arrow);
        deleteButton = (Button)findViewById(R.id.buttonDelete);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        favouriteIcon = (ImageView) findViewById(R.id.iconDetailFavourite);
        notFavouriteIcon = (ImageView) findViewById(R.id.iconDetailNotFavourite);
        layoutButtonComment = (LinearLayout) findViewById(R.id.layoutButtonComment);
        ratingStar = (RatingBar) findViewById(R.id.ratingStar);
        totalRatingStar = (RatingBar) findViewById(R.id.totalRatingStar);
        editComment = (EditText) findViewById(R.id.editComment);
        submitComment = (Button) findViewById(R.id.submitComment);
        resetComment = (Button) findViewById(R.id.resetComment);

        down_arrow = findViewById(R.id.down_arrow);
        third_scrollview = findViewById(R.id.third_scrillview);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        down_arrow.setAnimation(from_bottom);
        third_scrollview.setAnimation(from_bottom);

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentDetails.this, MainActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(down_arrow, "background_image_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CommentDetails.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        database = openOrCreateDatabase(dbName,MODE_PRIVATE,null);

        review = new Review();
        Bundle extras = getIntent().getExtras();

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
        cursor.close();

        if(sharedPreferenceConfig.read_login_status() == false || sharedPreferenceConfig.read_admin_status()) {
            editComment.setVisibility(View.INVISIBLE);
            submitComment.setVisibility(View.INVISIBLE);
            ((ViewGroup)resetComment.getParent()).removeView(resetComment);
            ((ViewGroup)layoutButtonComment.getParent()).removeView(layoutButtonComment);
            ((ViewGroup)ratingStar.getParent()).removeView(ratingStar);

            // Set constraint when remove editComment
            ConstraintLayout constraintLayout = findViewById(R.id.parentConstraintLayout);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
//            constraintSet.connect(R.id.down_arrow,ConstraintSet.TOP, R.id.lvComment,ConstraintSet.BOTTOM,50);
            constraintSet.applyTo(constraintLayout);
        }

        if(sharedPreferenceConfig.read_login_status() == false){
            favouriteIcon.setVisibility(View.INVISIBLE);
            notFavouriteIcon.setVisibility(View.INVISIBLE);

        }else{
            if(sharedPreferenceConfig.read_admin_status() == false){
                favouriteIcon.setVisibility(View.INVISIBLE);
                notFavouriteIcon.setVisibility(View.VISIBLE);
                Cursor cursorCheckIfFavourite = database.query("DanhSachYeuThich", null, null, null, null, null, null);

                while (cursorCheckIfFavourite.moveToNext()){
                    if(cursorCheckIfFavourite.getInt(1) == sharedPreferenceConfig.read_user_id() && cursorCheckIfFavourite.getInt(2) == review.id){
                        favouriteIcon.setVisibility(View.VISIBLE);
                        notFavouriteIcon.setVisibility(View.INVISIBLE);
                        //Toast.makeText(getApplicationContext(),"A",Toast.LENGTH_SHORT).show();
                    }

                }
                cursorCheckIfFavourite.close();
            }else{
                favouriteIcon.setVisibility(View.INVISIBLE);
                notFavouriteIcon.setVisibility(View.INVISIBLE);
            }

        }

        if(sharedPreferenceConfig.read_admin_status() == false){
            deleteButton.setVisibility(View.INVISIBLE);
            updateButton.setVisibility(View.INVISIBLE);

        }else{
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        }







        //Toast.makeText(getApplicationContext(),Integer.toString(extras.getInt("IDUser")),Toast.LENGTH_SHORT).show();





        notFavouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favouriteIcon.setVisibility(View.VISIBLE);
                notFavouriteIcon.setVisibility(View.INVISIBLE);

                if(sharedPreferenceConfig.read_user_id()  != 0){
                    //Toast.makeText(getApplicationContext(),Integer.toString(sharedPreferenceConfig.read_user_id()),Toast.LENGTH_SHORT).show();

                    Cursor cursor1 = database.query("DanhSachYeuThich", null, null, null, null, null, null);

                    while (cursor1.moveToNext()){
                        if(cursor1.getInt(1) == sharedPreferenceConfig.read_user_id() && cursor1.getInt(2) == review.id){
                            alreadyHasFavourited = true;

                        }

                    }
                    cursor1.close();
                    if(alreadyHasFavourited == false){
                        ContentValues values = new ContentValues();
                        values.put("IDUser", sharedPreferenceConfig.read_user_id());
                        values.put("IDDanhSachReview", review.id);

                        database.insert("DanhSachYeuThich",null, values );
                        Toast.makeText(getApplicationContext(),"Thêm vào danh sách yêu thích thành công",Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        favouriteIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                favouriteIcon.setVisibility(View.INVISIBLE);
                notFavouriteIcon.setVisibility(View.VISIBLE);
                if(sharedPreferenceConfig.read_user_id() != 0){
                    Cursor cursor1 = database.query("DanhSachYeuThich", null, null, null, null, null, null);

                    while (cursor1.moveToNext()){
                        if(cursor1.getInt(1) == sharedPreferenceConfig.read_user_id() && cursor1.getInt(2) == review.id){
                            database.delete("DanhSachYeuThich", "IDDanhSachReview=?", new String[]{Integer.toString(review.id)});
                            Toast.makeText(getApplicationContext(), "Xóa Khỏi Danh Sách Yêu Thích Thành Công", Toast.LENGTH_SHORT).show();
                        }

                    }
                    cursor1.close();
                }
            }
        });



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
            if(cursor2.getInt(0) == review.theloai ){
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

        // Get list of Comment & Total of rating star
        getListOfComment(extras.getInt("ID"));

        // Add or Edit a comment
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Add a comment
                if(selectedComment == -1) {
                    if(editComment.getText().length() > 0 && ratingStar.getRating() != 0) {
                        ContentValues values = new ContentValues();
                        values.put("NDBinhLuan", editComment.getText().toString());
                        values.put("IDUser", sharedPreferenceConfig.read_user_id());
                        values.put("IDDanhSachReview", extras.getInt("ID"));
                        values.put("DiemDanhGia", (int) ratingStar.getRating());

                        database.insert("BinhLuan",null, values);
                        editComment.setText("");
                        ratingStar.setRating(0);
                        selectedComment = -1;
                        getListOfComment(extras.getInt("ID"));
                        Toast.makeText(CommentDetails.this, "Thêm bình luận thành công!", Toast.LENGTH_SHORT).show();
                    }
                    else if(sharedPreferenceConfig.read_user_id() == 0) {
                        Toast.makeText(CommentDetails.this, "Đăng nhập trước khi bình luận!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(CommentDetails.this, "Vui lòng nhập bình luận và đánh giá sao!", Toast.LENGTH_SHORT).show();
                    }
                }

                // Update a comment
                else {
                    if(editComment.getText().length() > 0 && ratingStar.getRating() != 0) {
                        int id = comments.get(selectedComment).getId();
                        ContentValues values = new ContentValues();
                        values.put("NDBinhLuan", editComment.getText().toString());
                        values.put("IDUser", sharedPreferenceConfig.read_user_id());
                        values.put("IDDanhSachReview", extras.getInt("ID"));
                        values.put("DiemDanhGia", (int) ratingStar.getRating());

                        database.update("BinhLuan", values, "ID = " + id, null);
                        editComment.setText("");
                        ratingStar.setRating(0);
                        selectedComment = -1;
                        getListOfComment(extras.getInt("ID"));
                        Toast.makeText(CommentDetails.this, "Chỉnh sửa bình luận thành công!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(CommentDetails.this, "Vui lòng nhập bình luận và đánh giá sao!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // Reset to cancel comment
        resetComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedComment = -1;
                editComment.setText("");
                ratingStar.setRating(0);
            }
        });

        // Load comment selected
        lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Validate user info
                Comment cmt = new Comment();
                cmt.setUserId(comments.get(i).getUserId());

                if(sharedPreferenceConfig.read_user_id() == cmt.getUserId()) {
                    selectedComment = i;
                    OpenDeleteDialog(extras.getInt("ID"));
                }
                else if(sharedPreferenceConfig.read_admin_status()) {
                    selectedComment = i;
                    OpenDeleteDialog(extras.getInt("ID"));
                }
                else {
                    selectedComment = -1;
                }
            }
        });
    }

    public void getListOfComment(int currentExtras){
        comments = new ArrayList<Comment>();

        Cursor commentCursor = database.query("BinhLuan", null, null, null, null, null,  "ID DESC");

        while (commentCursor.moveToNext()){
            if(commentCursor.getInt(4) == currentExtras){
                Comment cmt = new Comment();

                //Set TenUser to the name
                Cursor userCursor = database.query("TaiKhoanUser", null, null, null, null, null, null);
                while (userCursor.moveToNext()){
                    if(userCursor.getInt(0) == commentCursor.getInt(2)){
                        cmt.setUserId(userCursor.getInt(0));
                        cmt.setName(userCursor.getString(3));
                    }
                }
                userCursor.close();

                cmt.setId(commentCursor.getInt(0));
                cmt.setComment(commentCursor.getString(1));
                cmt.setImageId(R.drawable.usericon);
                cmt.setScore(commentCursor.getInt(5));

                comments.add(cmt);
            }
        }
        commentCursor.close();

        // Get total of rating star
        int totalStar = 0;
        for (Comment cmt : comments) {
            totalStar += cmt.getScore();
        }
        totalRatingStar.setRating((float) totalStar / comments.size());

        // Update total rating to Review
        ContentValues values = new ContentValues();
        values.put("DanhGia", (float) totalStar / comments.size());
        database.update("DanhSachReview", values, "ID = " + currentExtras, null);

        adapter_comment listAdapterComment = new adapter_comment(comments, getApplicationContext());
        lvComment.setAdapter(listAdapterComment);
    }

    private void OpenDeleteDialog(int currentExtras) {
        if(selectedComment >= 0) {
            String comment = comments.get(selectedComment).getComment();

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CommentDetails.this);
            builder.setTitle("Thông báo")
                    .setMessage("Bạn có muốn xóa bình luận \"" + comment + "\" ?")
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int id = comments.get(selectedComment).getId();
                            database.delete("BinhLuan", "ID = " + id, null);
                            getListOfComment(currentExtras);
                            Toast.makeText(getBaseContext(), "Xóa bình luận thành công!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Comment cmt = new Comment();
                            cmt.setComment(comments.get(selectedComment).getComment());
                            cmt.setScore(comments.get(selectedComment).getScore());
                            editComment.setText(cmt.getComment());
                            ratingStar.setRating(cmt.getScore());
                        }
                    })
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedComment = -1;
                            editComment.setText("");
                            ratingStar.setRating(0);
                        }
                    }).create();

            // Hide update comment when admin login
            Button updateComment = builder.show().getButton(AlertDialog.BUTTON_NEUTRAL);
            if(sharedPreferenceConfig.read_admin_status()) {
                updateComment.setVisibility(View.INVISIBLE);
            }

        } else {
            Toast.makeText(getBaseContext(), "Hãy chọn 1 dòng để xóa!", Toast.LENGTH_SHORT).show();
        }
    }

    public void edit(View view) {
        Intent intent = new Intent(this, Edit.class);

        intent.putExtra("ID", review.id);

        startActivity(intent);
    }
}