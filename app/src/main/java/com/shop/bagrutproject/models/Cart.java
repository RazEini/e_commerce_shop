package com.shop.bagrutproject.models;

import java.util.List;

public class Cart {
    protected String id;
    protected List<Item> items;

    public Cart(String id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", items=" + items +
                '}';
    }
}
