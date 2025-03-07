package com.shop.bagrutproject.models;

public class Comment {

    private String userId;
    private String commentText;
    private float rating;

    public Comment() {

    }

    public Comment(String userId, String commentText, float rating) {
        this.userId = userId;
        this.commentText = commentText;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
