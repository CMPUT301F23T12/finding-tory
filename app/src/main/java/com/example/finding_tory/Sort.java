package com.example.finding_tory;

import java.io.Serializable;

public class Sort implements Serializable {
    private String sortType;
    private String sortOrder;

    public Sort() {
        this.sortType = "Description";
        this.sortOrder = "Ascending";
    }

    public Sort(String sortType, String sortOrder) {
        this.sortType = sortType;
        this.sortOrder = sortOrder;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
