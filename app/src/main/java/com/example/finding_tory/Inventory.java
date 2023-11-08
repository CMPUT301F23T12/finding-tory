package com.example.finding_tory;

import java.util.ArrayList;

public class Inventory {
    private String name;
    private ArrayList<String> items;  // TODO make this ArrayList<Item>
    private int count;
    private double value;

    public Inventory(String name) {
        this.name = name;
        this.count = 0;
        this.value = 0;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getItems() {
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

    public void setItems(ArrayList<String> items) {
        this.items = items;
        this.count = items.size();
        this.value++;  // TODO actually compute the new value
    }
}
