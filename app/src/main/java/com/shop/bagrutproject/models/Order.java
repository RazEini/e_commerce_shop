package com.shop.bagrutproject.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Order implements Serializable {
    private String orderId;
    private List<Item> items;
    private double totalPrice;
    private String status;
    private long timestamp;
    private String userId;

    public Order(String userId, List<Item> items) {
        this.orderId = UUID.randomUUID().toString();
        this.items = items != null ? items : new ArrayList<>();
        this.totalPrice = calculateTotalPrice();
        this.status = "Pending";
        this.timestamp = System.currentTimeMillis();
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

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(this.timestamp));
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(this.timestamp));
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
