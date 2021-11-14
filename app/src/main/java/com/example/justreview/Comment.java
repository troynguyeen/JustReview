package com.example.justreview;

public class Comment {
    private String name, comment;
    private int id, userId, score, idImage;

    public Comment() {

    }

    public Comment(int id, int userId, String name, String comment, int idImage, int score) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.comment = comment;
        this.idImage = idImage;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
