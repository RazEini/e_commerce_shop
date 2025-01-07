package com.shop.bagrutproject.models;

public class Comment {
    protected String id;
    protected User user;
    protected Item item;
    protected String date;
    protected String commentText;
    protected double rate;

    public Comment(String id, User user, Item item, String date, String commentText, double rate) {
        this.id = id;
        this.user = user;
        this.item = item;
        this.date = date;
        this.commentText = commentText;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", item=" + item +
                ", date='" + date + '\'' +
                ", commentText='" + commentText + '\'' +
                ", rate=" + rate +
                '}';
    }
}
