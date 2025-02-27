package com.shop.bagrutproject.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

    private List<Item> items;

    public Cart( List<Item> items) {

        this.items = items;
    }

    public Cart() {
        this.items = items;
    }

    public void addItem(Item item) {

        if(this.items==null){
            this.items=new ArrayList<>();
        }
        this.items.add(item);
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void removeItem(int index) {
        this.items.remove(index);
    }


    @Override
    public String toString() {
        return "Cart{" +

                ", items=" +this.items +
                '}';
    }
}
