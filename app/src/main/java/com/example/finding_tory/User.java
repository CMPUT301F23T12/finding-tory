package com.example.finding_tory;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a user with a username, name, password, and a list of inventories.
 * This class is serializable to support easy storage and retrieval.
 */
public class User implements Serializable {
    private String username;
    private String name;
    private String password;
    private ArrayList<Inventory> inventories;

    /**
     * Default constructor for creating an empty User instance.
     */
    public User() {
    }

    /**
     * Constructs a new User with specified username, name, and password.
     * Initializes an empty list of inventories.
     *
     * @param username the username of the user
     * @param name     the real name of the user
     * @param password the password of the user
     */
    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.inventories = new ArrayList<>();
    }

    public void addInventory(Inventory inventory) {
        inventories.add(inventory);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PropertyName("inventories")
    public ArrayList<Inventory> getInventories() {
        return inventories;
    }

    @PropertyName("inventories")
    public void setInventories(ArrayList<Inventory> inventories) {
        this.inventories = inventories;
    }
}
