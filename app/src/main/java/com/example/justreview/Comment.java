package com.example.justreview;

public class Comment {
    private String name, comment;
    private int score, idImage;

    public Comment() {

    }

    public Comment(String name, String comment, int idImage, int score) {
        this.name = name;
        this.comment = comment;
        this.idImage = idImage;
        this.score = score;
    }
    public int getImageId() {

        return idImage;
    }

    public void setImageId(int imageId) {

        this.idImage = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
