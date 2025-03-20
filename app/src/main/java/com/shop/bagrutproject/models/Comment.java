package com.shop.bagrutproject.models;

public class Comment {
    private String commentId;
    private String userId;
    private String commentText;
    private float rating;
    private String userName;

    public Comment() {

    }

    public Comment(String commentId, String userId, String commentText, float rating, String userName) {
        this.commentId = commentId;
        this.userId = userId;
        this.commentText = commentText;
        this.rating = rating;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
