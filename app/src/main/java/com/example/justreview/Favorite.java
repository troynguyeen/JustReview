package com.example.justreview;

public class Favorite {
    private String Title, Category;
    private int ImageId
//            , ImageTurned, ImageShare
            ;

    public Favorite() {

    }

    public Favorite(String title, String category, int imageId
//            , int imageTurned, int imageShare
    ) {
        this.Title = title;
        this.Category = category;
        this.ImageId = imageId;
//        this.ImageTurned = imageTurned;
//        this.ImageShare = imageShare;
    }

    public int getImageId() {

        return ImageId;
    }

    public void setImageId(int imageId) {

        this.ImageId = imageId;
    }

//    public void setImageTurned(int imageTurned) {
//
//        this.ImageTurned = imageTurned;
//    }
//
//    public int getImageTurned() {
//
//        return ImageTurned;
//    }
//
//    public void setImageShare(int imageShare) {
//
//        this.ImageShare = imageShare;
//    }
//
//    public int getImageShare() {
//
//        return ImageShare;
//    }

    public String getTitle() {

        return Title;
    }

    public void setTitle(String title) {

        Title = title;
    }

    public String getCategory() {

        return Category;
    }

    public void setCategory(String category) {

        Category = category;
    }
}

