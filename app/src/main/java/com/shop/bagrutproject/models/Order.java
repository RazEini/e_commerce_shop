package com.shop.bagrutproject.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {
    private String orderId;
    private List<Item> items;
    private double totalPrice;
    private String status;
    private long timestamp;
    private String userId; // כדי לדעת למי שייכת ההזמנה

    public Order(String userId, List<Item> items) {
        this.orderId = UUID.randomUUID().toString(); // מזהה ייחודי להזמנה
        this.items = items != null ? items : new ArrayList<>();
        this.totalPrice = calculateTotalPrice();
        this.status = "Pending"; // סטטוס ברירת מחדל
        this.timestamp = System.currentTimeMillis(); // זמן יצירת ההזמנה
        this.userId = userId;
    }

    public Order() {
        this.orderId = UUID.randomUUID().toString();
        this.items = new ArrayList<>();
        this.totalPrice = 0;
        this.status = "Pending";
        this.timestamp = System.currentTimeMillis();
    }

    private double calculateTotalPrice() {
        double sum = 0;
        for (Item item : items) {
            sum += item.getPrice();
        }
        return sum;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", items=" + items +
                '}';
    }
}

