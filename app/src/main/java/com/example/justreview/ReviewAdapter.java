package com.example.justreview;

import android.app.AppOpsManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private final List<Review> reviewList;
    OnReviewListener onReviewListener;

    public ReviewAdapter(List<Review> reviewList, OnReviewListener onReviewListener
    ) {
        this.reviewList = reviewList;
        this.onReviewListener = onReviewListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_book, parent ,false);
        return new ReviewViewHolder(view, onReviewListener);
    }



    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.setReview(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RoundedImageView imagePoster;
        private final TextView textName, textAuthor, textPostDate;
        private final RatingBar ratingBar;

        OnReviewListener onReviewListener;

        public ReviewViewHolder(View view, OnReviewListener onReviewListener) {
            super(view);
            imagePoster = view.findViewById(R.id.imagePoster);
            textName = view.findViewById(R.id.textName);
            textAuthor = view.findViewById(R.id.textAuthor);
            textPostDate = view.findViewById(R.id.textPostDate);
            ratingBar = view.findViewById(R.id.ratingBar);

            this.onReviewListener = onReviewListener;

            view.setOnClickListener(this);
        }

        void setReview(Review review) {
            if(review.image != null){
                byte[] bookImage = review.image;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bookImage, 0, bookImage.length);
                imagePoster.setImageBitmap(bitmap);
            }else{
                imagePoster.setImageResource(0);
            }

            textName.setText(review.name);
            textAuthor.setText(review.author);
            textPostDate.setText(review.postDate);
            ratingBar.setRating(review.rating);
        }

        @Override
        public void onClick(View view) {
            onReviewListener.onReviewClick(getAdapterPosition());
        }


    }

    public interface OnReviewListener{
        void onReviewClick(int position);
    }
}
