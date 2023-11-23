package com.example.finding_tory;

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

public class InventoryTest extends TestCase {
    private Inventory inventory;
    private Item testItem1;
    private Item testItem2;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory("Test Inventory");
        testItem1 = new Item(new Date(), "Item 1", "Make1", "Model1", 100.0f, "SN1", "Comment1", new ArrayList<>());
        testItem2 = new Item(new Date(), "Item 2", "Make2", "Model2", 200.0f, "SN2", "Comment2", new ArrayList<>());
        inventory.addItem(testItem1);
        inventory.addItem(testItem2);
    }

    public void testTestGetName() {
        assertEquals("Test Inventory", inventory.getInventoryName());
    }

    public void testGetItems() {
        assertEquals(2, inventory.getItems().size());
        assertTrue(inventory.getItems().contains(testItem1));
        assertTrue(inventory.getItems().contains(testItem2));
    }

    public void testGetCount() {
        assertEquals(2, inventory.getCount());
    }

    public void testGetValue() {
        inventory.calculateValue();
        assertEquals(300.0, inventory.getInventoryEstimatedValue());
    }

    public void testTestSetName() {
        inventory.setName("New Name");
        assertEquals("New Name", inventory.getInventoryName());
    }

    public void testSetItems() {
        ArrayList<Item> newItems = new ArrayList<>();
        newItems.add(testItem2);
        inventory.setItems(newItems);
        assertEquals(1, inventory.getCount());
        assertEquals(testItem2, inventory.getItems().get(0));
    }

    public void testGet() {
        assertEquals(testItem1, inventory.get(0));
    }

    public void testSet() {
        Item newItem = new Item(new Date(), "Test Item", "", "", 200f, "", "", new ArrayList<>());
        inventory.set(0, newItem);
        assertEquals(newItem, inventory.get(0));
    }

    public void testAddItem() {
        Item newItem = new Item(new Date(), "Test Item", "", "", 150f, "", "", new ArrayList<>());
        inventory.addItem(newItem);
        assertEquals(3, inventory.getCount());
        assertTrue(inventory.getItems().contains(newItem));
    }

    public void testRemoveItem() {
        inventory.removeItem(testItem1);
        assertEquals(1, inventory.getCount());
    }

    public void testRemoveItemByIndex() {
        inventory.removeItemByIndex(0);
        assertEquals(1, inventory.getCount());
        assertTrue(inventory.getItems().contains(testItem2));
    }
}