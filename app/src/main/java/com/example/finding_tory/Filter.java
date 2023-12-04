package com.example.finding_tory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * The {@code Filter} class represents a filtering mechanism for data.
 * It contains criteria such as start date, end date, description, make, and a list of tags.
 * These criteria can be used to filter an inventory based on specific attributes and time frames.
 */
public class Filter implements Serializable {
    private Date startDate;
    private Date endDate;
    private String description;
    private String make;
    private ArrayList<String> tags;

    /**
     * Constructs a new, empty Filter with default values.
     *
     * This constructor initializes a Filter object with empty strings for the description and make fields,
     * and an empty ArrayList for tags. The start and end dates are not initialized and remain null.
     * This constructor can be used when you want to create a Filter object and set its properties later.
     */
    public Filter() {
        this.description = "";
        this.make = "";
        this.tags = new ArrayList<>();
    }

    /**
     * Constructs a new Filter with specified start date, end date, description, make, and tags.
     *
     * @param startDate   The start date of the filter period.
     * @param endDate     The end date of the filter period.
     * @param description The description to filter by.
     * @param make        The make (or brand) to filter by.
     * @param tags        The list of tags to filter by.
     */
    public Filter(Date startDate, Date endDate, String description, String make, ArrayList<String> tags) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.make = make;
        this.tags = tags;
    }

    /**
     * Returns the start date of the filter.
     *
     * @return The start date.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the filter.
     *
     * @param startDate The start date to set.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the filter.
     *
     * @return The end date.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the filter.
     *
     * @param endDate The end date to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the description used in the filter.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for the filter.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the make used in the filter.
     *
     * @return The make.
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the make for the filter.
     *
     * @param make The make to set.
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Returns the list of tags used in the filter.
     *
     * @return The list of tags.
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * Sets the list of tags for the filter.
     *
     * @param tags The list of tags to set.
     */
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     * Checks if the filter is empty (i.e., all its fields are either null or empty).
     *
     * @return {@code true} if all fields are null or empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return startDate == null && endDate == null && description.isEmpty() && make.isEmpty() && tags.isEmpty();
    }
}
