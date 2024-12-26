package com.shop.bagrutproject.model;

import java.util.List;

public class Item {

    String id;
    String type;
    String company;
    String modelItem;
    String color;
    double price;
    String aboutItem;
    List<String> pics;
    int numberRate;
    double rate;
    double sumRate;

    public Item() {
    }

    public Item(String id, String type, String company, String modelItem, String color, double price, String aboutItem, List<String> pics, int numberRate, double rate, double sumRate) {
        this.id = id;
        this.type = type;
        this.company = company;
        this.modelItem = modelItem;
        this.color = color;
        this.price = price;
        this.aboutItem = aboutItem;
        this.pics = pics;
        this.numberRate = numberRate;
        this.rate = rate;
        this.sumRate = sumRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModelItem() {
        return modelItem;
    }

    public void setModelItem(String modelItem) {
        this.modelItem = modelItem;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAboutItem() {
        return aboutItem;
    }

    public void setAboutItem(String aboutItem) {
        this.aboutItem = aboutItem;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public int getNumberRate() {
        return numberRate;
    }

    public void setNumberRate(int numberRate) {
        this.numberRate = numberRate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getSumRate() {
        return sumRate;
    }

    public void setSumRate(double sumRate) {
        this.sumRate = sumRate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", company='" + company + '\'' +
                ", modelItem='" + modelItem + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", aboutItem='" + aboutItem + '\'' +
                ", pics=" + pics +
                ", numberRate=" + numberRate +
                ", rate=" + rate +
                ", sumRate=" + sumRate +
                '}';
    }
}
