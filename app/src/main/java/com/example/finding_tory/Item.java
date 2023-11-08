package com.example.finding_tory;

public class Item {
    private String description;
    private double value;
    private String tags;

    public Item(String description, double value, String tags) {
        this.description = description;
        this.value = value;
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
