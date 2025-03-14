package com.shop.bagrutproject.models;

public class Comment {
    private String commentId; // מזהה תגובה
    private String userId;
    private String commentText;
    private float rating;

    public Comment() {
    }

    public Comment(String commentId, String userId, String commentText, float rating) {
        this.commentId = commentId;
        this.userId = userId;
        this.commentText = commentText;
        this.rating = rating;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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
