package com.shop.bagrutproject.models;

import org.jetbrains.annotations.NotNull;

public class User {

    /// unique id of the user
    protected String uid;
    protected String email;
    protected String password;
    protected String fName;
    protected String lName;
    protected String phone;

    protected Cart cart;

    public User() {
        cart = new Cart();
    }

    public User(String uid, String email, String password, String fName, String lName, String phone, Cart cart) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.cart = cart;
    }

    public User(String uid, String email, String password, String fName, String lName, String phone) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;

    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", phone='" + phone + '\'' +
                ", cart=" + cart +
                '}';
    }
}
