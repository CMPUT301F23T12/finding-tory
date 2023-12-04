package com.example.finding_tory;

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class LedgerTest extends TestCase {
    private Ledger ledger;
    private Inventory testInventory;
    private User testUser;

    @BeforeEach
    public void setUp() {
        ledger = Ledger.getInstance();
        testInventory = new Inventory("Test Inventory");
        testUser = new User("TestUser", "test@example.com", "pwd");
        ArrayList<Inventory> inventories = new ArrayList<>();
        inventories.add(testInventory);
        ledger.setInventories(inventories);
        ledger.setUser(testUser);
    }

    @Test
    public void testSingletonInstance() {
        Ledger anotherInstance = Ledger.getInstance();
        assertSame(ledger, anotherInstance);
    }

    @Test
    public void testGetInventories() {
        assertNotNull(ledger.getInventories());
        assertEquals(1, ledger.getInventories().size());
        assertEquals(testInventory, ledger.getInventories().get(0));
    }

    @Test
    public void testSetInventories() {
        Inventory anotherInventory = new Inventory("Another Inventory");
        ArrayList<Inventory> newInventories = new ArrayList<>();
        newInventories.add(anotherInventory);
        ledger.setInventories(newInventories);
        assertEquals(1, ledger.getInventories().size());
        assertEquals(anotherInventory, ledger.getInventories().get(0));
    }

    @Test
    public void testGetUser() {
        assertEquals(testUser, ledger.getUser());
    }

    @Test
    public void testSetUser() {
        User newUser = new User("NewUser", "new@example.com", "pwd");
        ledger.setUser(newUser);
        assertEquals(newUser, ledger.getUser());
    }

    @Test
    public void testSetAndGetUserNames() {
        ledger.setUserNames("John Doe", "jdoe");
        assertEquals("John Doe", ledger.getUsrName());
        assertEquals("jdoe", ledger.getUsrIDName());
    }

    @Test
    public void testDeleteInventory() {
        ledger.deleteInventory(testInventory);
        assertFalse(ledger.getInventories().contains(testInventory));
        assertEquals(0, ledger.getInventories().size());
    }
}
