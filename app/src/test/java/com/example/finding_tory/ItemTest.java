package com.example.finding_tory;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ItemTest extends TestCase {
    private Item item;
    private Date purchaseDate;
    private String description;
    private String make;
    private String model;
    private String serialNumber;
    private float estimatedValue;
    private String comment;
    private ArrayList<String> itemTags;

    @BeforeEach
    public void setUp() throws ParseException {
        purchaseDate = new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01");
        description = "computer";
        make = "MacBook";
        model = "Air";
        serialNumber = "548FF45";
        estimatedValue = 1799.99f;
        comment = "This is a comment";
        itemTags = new ArrayList<>(Arrays.asList("Home", "School"));
        item = new Item(purchaseDate, description, make, model, estimatedValue, serialNumber, comment, itemTags, new ArrayList<>());
    }

    @Test
    public void testUpdateItem() {
        Item newItem = new Item(new Date(), "New Description", "New Make", "New Model", 1f, "NewSerial", "New Comment", new ArrayList<>(), new ArrayList<>());
        item.updateItem(newItem);
        assertEquals(newItem.getPurchaseDate(), item.getPurchaseDate());
        assertEquals(newItem.getDescription(), item.getDescription());
        assertEquals(newItem.getMake(), item.getMake());
        assertEquals(newItem.getModel(), item.getModel());
        assertEquals(newItem.getSerialNumber(), item.getSerialNumber());
        assertEquals(newItem.getEstimatedValue(), item.getEstimatedValue());
        assertEquals(newItem.getComment(), item.getComment());
        assertIterableEquals(newItem.getItemTags(), item.getItemTags());
    }

    @Test
    public void testTestGetPurchaseDate() {
        assertEquals(purchaseDate, item.getPurchaseDate());
    }

    @Test
    public void testTestSetPurchaseDate() {
        Date newPurchaseDate = new Date();
        item.setPurchaseDate(newPurchaseDate);
        assertEquals(newPurchaseDate, item.getPurchaseDate());
    }

    @Test
    public void testTestGetDescription() {
        assertEquals(description, item.getDescription());
    }

    @Test
    public void testTestSetDescription() {
        String newDescription = "New description";
        item.setDescription(newDescription);
        assertEquals(newDescription, item.getDescription());
    }

    @Test
    public void testTestGetMake() {
        assertEquals(make, item.getMake());
    }

    @Test
    public void testTestSetMake() {
        String newMake = "New make";
        item.setMake(newMake);
        assertEquals(newMake, item.getMake());
    }

    @Test
    public void testTestGetModel() {
        assertEquals(model, item.getModel());
    }

    @Test
    public void testTestSetModel() {
        String newModel = "New model";
        item.setModel(newModel);
        assertEquals(newModel, item.getModel());
    }

    @Test
    public void testTestGetSerialNumber() {
        assertEquals(serialNumber, item.getSerialNumber());
    }

    @Test
    public void testTestSetSerialNumber() {
        String newSN = "New serial number";
        item.setSerialNumber(newSN);
        assertEquals(newSN, item.getSerialNumber());
    }

    @Test
    public void testTestGetEstimatedValue() {
        assertEquals(estimatedValue, item.getEstimatedValue());
    }

    @Test
    public void testTestSetEstimatedValue() {
        float newValue = 100.00f;
        item.setEstimatedValue(newValue);
        assertEquals(newValue, item.getEstimatedValue());
    }

    @Test
    public void testTestGetComment() {
        assertEquals(comment, item.getComment());
    }

    @Test
    public void testTestSetComment() {
        String newComment = "New comment";
        item.setComment(newComment);
        assertEquals(newComment, item.getComment());
    }

    @Test
    public void testTestGetItemTags() {
        assertIterableEquals(itemTags, item.getItemTags());
    }

    @Test
    public void testGetTagsString() {
        StringBuilder expectedTagsString = new StringBuilder();
        for (String tag : itemTags) {
            expectedTagsString.append(tag).append(" ");
        }
        assertEquals(expectedTagsString.toString().trim(), item.getTagsString().toString().trim());
    }

    @Test
    public void testTestSetItemTags() {
        ArrayList<String> newItemTags = new ArrayList<>(Arrays.asList("tag1", "tag2"));
        ArrayList<String> resultTags = new ArrayList<>(Arrays.asList("Tag1", "Tag2"));
        item.setItemTags(newItemTags);
        assertIterableEquals(resultTags, item.getItemTags());
    }

    @Test
    public void testErrorHandleItemInput() throws ParseException {
        String errorMessage = Item.errorHandleItemInput("2023-01-01", "Phone", "500");
        assertTrue(errorMessage.isEmpty());

        // Test for empty description
        errorMessage = Item.errorHandleItemInput("2023-01-01", "", "500");
        assertEquals("Item Description cannot be empty", errorMessage);

        // Test for future date
        errorMessage = Item.errorHandleItemInput("2024-01-01", "Phone", "500");
        assertEquals("Date cannot be in the future", errorMessage);

        // Test for empty estimated value
        errorMessage = Item.errorHandleItemInput("2023-01-01", "Phone", "");
        assertEquals("Estimated value cannot be empty", errorMessage);

        // Test for negative estimated value
        errorMessage = Item.errorHandleItemInput("2023-01-01", "Phone", "-500");
        assertEquals("Cannot have a negative Estimated value", errorMessage);
    }
}
