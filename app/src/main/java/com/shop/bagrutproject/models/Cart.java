package com.shop.bagrutproject.models;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private String id;
    private List<Item> items;

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
}
