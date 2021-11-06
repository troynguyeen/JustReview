package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class CommentDetails extends AppCompatActivity {
    ListView lvComment;
    ArrayList<Comment> comments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);

        lvComment = (ListView) findViewById(R.id.lvComment);
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
}