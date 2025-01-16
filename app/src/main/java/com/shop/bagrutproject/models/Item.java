package com.shop.bagrutproject.models;

import java.util.List;

public class Item {

    protected String id;

    protected String name;
    protected String type;
    protected String color;
    protected String company;
    protected String aboutItem;
    protected double price;
    protected String pic;
    protected int numberRate;
    protected double rate;
    protected double sumRate;

    public Item() {
    }

    public Item(String id, String name, String type, String color, String company, String aboutItem, double price, String pic, int numberRate, double rate, double sumRate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
        this.company = company;
        this.aboutItem = aboutItem;
        this.price = price;
        this.pic = pic;
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

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", company='" + company + '\'' +
                ", aboutItem='" + aboutItem + '\'' +
                ", price=" + price +

                ", numberRate=" + numberRate +
                ", rate=" + rate +
                ", sumRate=" + sumRate +
                '}';
    }
}
