package com.example.finding_tory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The {@code Sort} class represents sorting parameters.
 * It is used to define sorting criteria, such as sort type and order.
 * This class implements {@link Serializable} to allow its objects
 * to be serialized.
 */
public class Sort implements Serializable {
    private String sortType;
    private String sortOrder;

    /**
     * Constructs a new {@code Sort} object with default sorting parameters.
     * The default sort type is set to "Description" and the sort order is set to "Ascending".
     */
    public Sort() {
        this.sortType = "Description";
        this.sortOrder = "Ascending";
    }

    /**
     * Constructs a new {@code Sort} object with specified sorting parameters.
     *
     * @param sortType  the type of sorting (e.g., "Date", "Description", etc.)
     * @param sortOrder the order of sorting, either "Ascending" or "Descending"
     */
    public Sort(String sortType, String sortOrder) {
        setSortType(sortType);
        setSortOrder(sortOrder);
    }

    /**
     * Returns the sort type of this {@code Sort} object.
     *
     * @return the sort type
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * Sets the sort type of this {@code Sort} object.
     *
     * @param sortType the type of sorting to set
     */
    public void setSortType(String sortType) {
        ArrayList<String> validSortTypes = new ArrayList<>(Arrays.asList("Date", "Description", "Make", "Value", "Tags"));
        if (validSortTypes.contains(sortType))
            this.sortType = sortType;
        else
            this.sortType = "Description";
    }

    /**
     * Returns the sort order of this {@code Sort} object.
     *
     * @return the sort order
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the sort order of this {@code Sort} object.
     *
     * @param sortOrder the order of sorting to set, either "Ascending" or "Descending"
     */
    public void setSortOrder(String sortOrder) {
        ArrayList<String> validSortOrders = new ArrayList<>(Arrays.asList("Ascending", "Descending"));
        if (validSortOrders.contains(sortOrder))
            this.sortOrder = sortOrder;
        else
            this.sortOrder = "Ascending";
    }
}
