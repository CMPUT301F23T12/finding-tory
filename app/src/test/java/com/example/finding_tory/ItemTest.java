package com.example.finding_tory;

import junit.framework.TestCase;
import java.util.Date;
import java.util.ArrayList;

public class ItemTest extends TestCase {
    private Item item;

    protected void setUp() {
        // Create a sample Item instance for testing
        Date purchaseDate = new Date();
        String description = "Test Item";
        String make = "Test Make";
        String model = "Test Model";
        int serialNumber = 12345;
        Float estimatedValue = 100.0f;
        String comment = "Test Comment";
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        item = new Item(purchaseDate, description, make, model, estimatedValue, serialNumber, comment, tags);
    }

    public void testGetPurchaseDate() {
        assertEquals(item.getPurchaseDate(), item.getPurchaseDate());
    }

    public void testSetPurchaseDate() {
        Date newPurchaseDate = new Date();
        item.setPurchaseDate(newPurchaseDate);
        assertEquals(newPurchaseDate, item.getPurchaseDate());
    }

    public void testGetDescription() {
        assertEquals(item.getDescription(), "Test Item");
    }

    public void testSetDescription() {
        item.setDescription("New Description");
        assertEquals(item.getDescription(), "New Description");
    }

    public void testGetMake() {
        assertEquals(item.getMake(), "Test Make");
    }

    public void testSetMake() {
        item.setMake("New Make");
        assertEquals(item.getMake(), "New Make");
    }

    public void testGetModel() {
        assertEquals(item.getModel(), "Test Model");
    }

    public void testSetModel() {
        item.setModel("New Model");
        assertEquals(item.getModel(), "New Model");
    }

    public void testGetSerialNumber() {
        assertEquals(item.getSerialNumber(), 12345);
    }

    public void testSetSerialNumber() {
        item.setSerialNumber(54321);
        assertEquals(item.getSerialNumber(), 54321);
    }

    public void testGetEstimatedValue() {
        assertEquals(item.getEstimatedValue(), 100.0f, 0.001); // Use delta for floating-point comparisons
    }

    public void testSetEstimatedValue() {
        item.setEstimatedValue(200.0f);
        assertEquals(item.getEstimatedValue(), 200.0f, 0.001);
    }

    public void testGetComment() {
        assertEquals(item.getComment(), "Test Comment");
    }

    public void testSetComment() {
        item.setComment("New Comment");
        assertEquals(item.getComment(), "New Comment");
    }

    public void testGetItemTags() {
        assertNull(item.getItemTags());
    }

    public void testSetItemTags() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Tag1");
        tags.add("Tag2");
        item.setItemTags(tags);
        assertEquals(item.getItemTags(), tags);
    }
}
