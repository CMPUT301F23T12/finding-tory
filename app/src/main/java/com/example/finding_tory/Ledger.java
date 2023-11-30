package com.example.finding_tory;

import java.util.ArrayList;

public class Ledger {
    private static final Ledger instance = new Ledger(new ArrayList<Inventory>(), new User());

    private ArrayList<Inventory> inventories;
    private User user;

    private Ledger(ArrayList<Inventory> inventories, User user) {
        this.inventories = inventories;
        this.user = user;
    }

    public static Ledger getInstance() {
        return instance;
    }

    public ArrayList<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(ArrayList<Inventory> inventories) {
        this.inventories = inventories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
