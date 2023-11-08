package com.example.finding_tory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Item implements Serializable {
    Date purchaseDate;
    String description;
    String make;
    String model;
    int serialNumber;
    Float estimatedValue;
    String comment;
    ArrayList<String> itemTags;
    // TODO: Add ArrayList of Images


    public Item(Date purchaseDate, String description, String make, String model, Float estimatedValue, int serialNumber, String comment, ArrayList<String> itemTags) {
        this.purchaseDate = purchaseDate;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        this.itemTags = itemTags;
    }

    public Item(Date purchaseDate, String description, String make, String model, Float estimatedValue, String comment, ArrayList<String> itemTags) {
        this.purchaseDate = purchaseDate;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = 0;
        this.estimatedValue = estimatedValue;
        this.comment = comment;
        this.itemTags = itemTags;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Float getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Float estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getItemTags() {
        return itemTags;
    }

    public void setItemTags(ArrayList<String> itemTags) {
        this.itemTags = itemTags;
    }
}
