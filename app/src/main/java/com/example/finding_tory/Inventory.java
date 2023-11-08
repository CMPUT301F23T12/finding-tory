package com.example.finding_tory;

import java.util.ArrayList;

public class Inventory {
    private String name;
    private ArrayList<Item> items;
    private int count;
    private double value;

    public Inventory(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.count = 0;
        this.value = 0;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int getCount() {
        return count;
    }

    public double getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        this.count = items.size();
        this.value = 0;
        for (Item item : items) {
            this.value += item.getValue();
        }
    }

    public void addItem(Item item) {
        this.items.add(item);
        this.count++;
        this.value += item.getValue();
    }
}
