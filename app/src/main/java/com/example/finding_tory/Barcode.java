package com.example.finding_tory;

/**
 * Represents the data that is associated with a barcode such as description, make, model, and cost.
 */
public class Barcode {
    private String product_number;
    private String description;
    private String make;
    private String model;
    private String cost;

    /**
     * Constructs an Item with specified attributes.
     *
     * @param product_number The product number of scanned barcode
     * @param description    The description of the item.
     * @param make           The make of the item.
     * @param model          The model of the item.
     * @param cost           The cost of the item.
     */
    public Barcode(String product_number, String description, String make, String model, String cost) {
        this.product_number = product_number;
        this.description = description;
        this.make = make;
        this.model = model;
        this.cost = cost;
    }

    /**
     * Gets the description of item
     *
     * @return item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the make of item
     *
     * @return item make
     */
    public String getMake() {
        return make;
    }

    /**
     * Gets the model of item
     *
     * @return item model
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the cost of item
     *
     * @return item cost
     */
    public String getCost() {
        return cost;
    }

    /**
     * Checks to see if number returned from scanned item is equal to the product number stored
     *
     * @return if this object has the same product number
     */
    public boolean equals(String obj) {
        return this.product_number.equals(obj);
    }
}
