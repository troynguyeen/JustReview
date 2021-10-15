package com.example.justreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupReviewViewPager();
    }

    public void goToFavouritePage(){

    }
    private void setupReviewViewPager() {
        ViewPager2 reviewViewPager = findViewById(R.id.reviewViewPager);
        reviewViewPager.setClipToPadding(false);
        reviewViewPager.setClipChildren(false);
        reviewViewPager.setOffscreenPageLimit(4);
        reviewViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        }));
        reviewViewPager.setPageTransformer(compositePageTransformer);
        reviewViewPager.setAdapter(new ReviewAdapter(getReview()));
    }

    private List<Review> getReview() {
        List<Review> reviewList = new ArrayList<>();

        Review book_codedaokysu = new Review();
        book_codedaokysu.poster = R.drawable.book_codedao;
        book_codedaokysu.name = "Code dạo ký sự";
        book_codedaokysu.author = "Phạm Huy Hoàng";
        book_codedaokysu.postDate = "03/10/2021";
        book_codedaokysu.rating = 4.5f;
        reviewList.add(book_codedaokysu);

        Review book_hacknao1500tuvung = new Review();
        book_hacknao1500tuvung.poster = R.drawable.book_hacknao1500tuvung;
        book_hacknao1500tuvung.name = "HACK NÃO 1500";
        book_hacknao1500tuvung.author = "STEPUP";
        book_hacknao1500tuvung.postDate = "05/10/2021";
        book_hacknao1500tuvung.rating = 4;
        reviewList.add(book_hacknao1500tuvung);

        Review book_hacknao_ielts = new Review();
        book_hacknao_ielts.poster = R.drawable.book_hacknao_ielts;
        book_hacknao_ielts.name = "HACK NÃO IELTS";
        book_hacknao_ielts.author = "STEPUP";
        book_hacknao_ielts.postDate = "10/10/2021";
        book_hacknao_ielts.rating = 3.5f;
        reviewList.add(book_hacknao_ielts);

        Review book_hoang_tu_be = new Review();
        book_hoang_tu_be.poster = R.drawable.book_hoang_tu_be;
        book_hoang_tu_be.name = "Hoàng tử bé";
        book_hoang_tu_be.author = "Antoine De Saint-Exupéry";
        book_hoang_tu_be.postDate = "01/11/2021";
        book_hoang_tu_be.rating = 4.5f;
        reviewList.add(book_hoang_tu_be);

        return reviewList;
    }
}