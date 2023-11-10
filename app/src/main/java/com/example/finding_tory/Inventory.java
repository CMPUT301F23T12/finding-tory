package com.example.finding_tory;

import java.util.ArrayList;

/**
 * Container class to hold a list of Items.
 */
public class Inventory {

    private String name;
    private ArrayList<Item> items;
    private double value;

    /**
     * Creates a new Inventory object.
     *
     * @param name name for the inventory
     */
    public Inventory(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.value = 0;
    }


    /**
     * Gets the name of the inventory.
     *
     * @return inventory name
     */
    public String getName() {
        return name;
    }


    /**
     * Gets the list of Items stored by the inventory.
     *
     * @return ArrayList holding the inventory's items
     */
    public ArrayList<Item> getItems() {
        return items;
    }


    /**
     * Gets the number of Items being held by the inventory.
     *
     * @return size of the inventory
     */
    public int getCount() {
        return items.size();
    }


    /**
     * Gets the total value of all the Items in the inventory.
     *
     * @return sum of all item values
     */
    public double getValue() {
        return value;
    }


    /**
     * Sets the name of the inventory object.
     *
     * @param name new name to rename to
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Sets the list of Items being held in the inventory, overwriting any previous lists.
     *
     * @param items new ArrayList of Items to store
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
        this.value = 0;
        for (Item item : items) {
            this.value += item.getEstimatedValue();
        }
    }


    /**
     * Adds an Item object to the list of stored items in the inventory.
     *
     * @param item new Item object to add to inventory
     */
    public void addItem(Item item) {
        this.items.add(item);
        this.value += item.getEstimatedValue();
    }

    public void removeItem(Item item) {
        this.items.remove(item);
        this.value -= item.getEstimatedValue();
    }

    public void removeItemByIndex(int i) {
        this.value -= this.items.get(i).getEstimatedValue();
        this.items.remove(i);
    }
}
