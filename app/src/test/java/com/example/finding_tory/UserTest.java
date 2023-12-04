package com.example.finding_tory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import junit.framework.TestCase;

import java.util.ArrayList;

public class UserTest extends TestCase {
    private User user;
    private Inventory testInventory;

    @BeforeEach
    public void setUp() {
        user = new User("user123", "John Doe", "password123");
        testInventory = new Inventory("Test Inventory");
    }

    @Test
    public void testParameterizedConstructor() {
        assertEquals("user123", user.getUsername());
        assertEquals("John Doe", user.getName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.getInventories().isEmpty());
    }

    @Test
    public void testSetAndGetUsername() {
        user.setUsername("newuser123");
        assertEquals("newuser123", user.getUsername());
    }

    @Test
    public void testSetAndGetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    public void testSetAndGetPassword() {
        user.setPassword("newpassword123");
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    public void testAddAndGetInventories() {
        user.addInventory(testInventory);
        assertEquals(1, user.getInventories().size());
        assertEquals(testInventory, user.getInventories().get(0));
    }

    @Test
    public void testSetInventories() {
        ArrayList<Inventory> newInventories = new ArrayList<>();
        newInventories.add(testInventory);
        user.setInventories(newInventories);
        assertEquals(1, user.getInventories().size());
        assertEquals(testInventory, user.getInventories().get(0));
    }
}
