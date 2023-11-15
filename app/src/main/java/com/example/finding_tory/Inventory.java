package com.example.finding_tory;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Represents an inventory containing a collection of items. This class provides functionalities to manage
 * an inventory, such as adding, removing, and accessing items. It also calculates the total estimated value of all
 * items in the inventory.
 */
public class Inventory implements Serializable {

    private String inventoryName;
    private ArrayList<Item> items;
    private double inventoryEstimatedValue;
    private int inventoryTotalItems;
    private ArrayList<String> allTags;
    private String sortType = "Description";
    private String sortOrder = "Ascending";

    public Inventory() {}

    /**
     * Constructs a new Inventory object with a specified name.
     * Initializes an empty list of items and sets the total value to zero.
     *
     * @param name The name of the inventory.
     */
    public Inventory(String name) {
        this.inventoryName = name;
        this.items = new ArrayList<>();
        this.inventoryEstimatedValue = 0;
        this.inventoryTotalItems = 0;
        this.allTags = new ArrayList<>();
    }

    /**
     * Gets the name of the inventory.
     *
     * @return inventory name
     */
    @PropertyName("inventoryName")
    public String getInventoryName() {
        return inventoryName;
    }

    /**
     * Gets the list of Items stored by the inventory.
     *
     * @return ArrayList holding the inventory's items
     */

    @PropertyName("items")
    public ArrayList<Item> getItems() {
        return items;
    }

    @PropertyName("inventoryEstimatedValue")
    public double getInventoryEstimatedValue() {
        return inventoryEstimatedValue;
    }

    @PropertyName("inventoryTotalItems")
    public int getInventoryTotalItems() {
        return inventoryTotalItems;
    }

    /**
     * Gets the number of Items being held by the inventory.
     *
     * @return size of the inventory
     */
    @PropertyName("itemsCount")
    public int getCount() {
        return items.size();
    }

    /**
     * Gets the total value of all the Items in the inventory.
     *
     * @return sum of all item values
     */

    /**
     * Sets the name of the inventory object.
     *
     * @param name new name to rename to
     */
    public void setName(String name) {
        this.inventoryName = name;
    }


    /**
     * Recalculates the total value of all items in the inventory.
     * This method should be called whenever the list of items is modified.
     */
    public void calculateValue() {
        this.inventoryEstimatedValue = 0;
        for (Item item : items) {
            this.inventoryEstimatedValue += item.getEstimatedValue();
        }
    }

    public void setInventoryTotalItems() {
        this.inventoryTotalItems = this.items.size();
        System.out.println(this.items.size());
    }

    /**
     * Sets the list of Items being held in the inventory, overwriting any previous lists.
     * Also recalculates the total inventory value based on the new items.
     *
     * @param items new ArrayList of Items to store
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
        calculateValue();
        this.allTags = new ArrayList<>();
        for (Item item : items) {
            for (String s : item.getItemTags()) {
                if (!this.allTags.contains(s)) {
                    this.allTags.add(s);
                }
            }
        }
    }

    /**
     * Retrieves an item at a specific index in the inventory.
     *
     * @param index The index of the item to retrieve.
     * @return The item at the specified index.
     */
    public Item get(int index) {
        return this.items.get(index);
    }

    /**
     * Replaces an item at a specific index in the inventory with a new item.
     * Also recalculates the total inventory value after the replacement.
     *
     * @param index The index of the item to replace.
     * @param item  The new item to set in the inventory.
     */
    public void set(int index, Item item) {
        items.set(index, item);
        calculateValue();
    }

    /**
     * Adds a new item to the inventory and updates the total value.
     *
     * @param item The item to add to the inventory.
     */
    public void addItem(Item item) {
        this.items.add(item);
        for (String s : item.getItemTags()) {
            if (!this.allTags.contains(s)) {
                this.allTags.add(s);
            }
        }
        Collections.sort(this.allTags);
        this.inventoryEstimatedValue += item.getEstimatedValue();
    }

    /**
     * Removes an item from the inventory and updates the total value, and tags.
     *
     * @param item The item to remove from the inventory.
     */
    public void removeItem(Item item) {
        for (String tag : item.getItemTags()) {
            this.allTags.remove(tag);
        }
        this.items.remove(item);
        this.inventoryEstimatedValue -= item.getEstimatedValue();
    }

    /**
     * Removes an item at a specific index from the inventory and updates the total value.
     *
     * @param i The index of the item to remove.
     */
    public void removeItemByIndex(int i) {
        System.out.println(i);
        for (String tag : this.items.get(i).getItemTags()) {
            this.allTags.remove(tag);
        }
        this.inventoryEstimatedValue -= this.items.get(i).getEstimatedValue();
        this.items.remove(i);
    }

    public void addTagsToInventory(ArrayList<String> newTags){
        for (String s : newTags){
            if(!allTags.contains(s)){
                newTags.add(s);
            }
        }
    }

    public void setSortData(String sortType, String sortOrder) {
        this.sortType = sortType;
        this.sortOrder = sortOrder;
    }

    public ArrayList<String> getAllTags() {
        return this.allTags;
    }

    public Boolean sortItems() {
        Comparator<Item> comparator;
        switch (this.sortType) {
            case "Description":
                comparator = Comparator.comparing(item -> item.getDescription().toLowerCase());
                break;
            case "Date":
                comparator = Comparator.comparing(Item::getPurchaseDate);
                break;
            case "Make":
                comparator = Comparator.comparing(item -> item.getMake().toLowerCase());
                break;
            case "Value":
                comparator = Comparator.comparing(Item::getEstimatedValue);
                break;
            default:
                return false;
        }
        if ("Descending".equals(this.sortOrder)) {
            comparator = comparator.reversed();
        }
        Collections.sort(items, comparator);
        return true;
    }

    public void printItems() {
        String s = "";
        for (Item i : items) {
            s += i.getDescription() + ", ";
        }
        System.out.println(s);
    }
}
