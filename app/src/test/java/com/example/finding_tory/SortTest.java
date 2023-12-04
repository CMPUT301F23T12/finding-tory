package com.example.finding_tory;

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SortTest extends TestCase {
    private Sort sort;

    @BeforeEach
    public void setUp() {
        sort = new Sort();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals("Description", sort.getSortType());
        assertEquals("Ascending", sort.getSortOrder());
    }

    @Test
    public void testParameterizedConstructor() {
        Sort customSort = new Sort("Date", "Descending");
        assertEquals("Date", customSort.getSortType());
        assertEquals("Descending", customSort.getSortOrder());
    }

    @Test
    public void testSetAndGetSortType() {
        sort.setSortType("Make");
        assertEquals("Make", sort.getSortType());

        // Test defaulting to "Description" for invalid sort type
        sort.setSortType("InvalidType");
        assertEquals("Description", sort.getSortType());
    }

    @Test
    public void testSetAndGetSortOrder() {
        sort.setSortOrder("Descending");
        assertEquals("Descending", sort.getSortOrder());

        // Test defaulting to "Ascending" for invalid sort order
        sort.setSortOrder("InvalidOrder");
        assertEquals("Ascending", sort.getSortOrder());
    }

    @Test
    public void testValidSortTypes() {
        sort.setSortType("Date");
        assertEquals("Date", sort.getSortType());

        sort.setSortType("Value");
        assertEquals("Value", sort.getSortType());

        sort.setSortType("Tags");
        assertEquals("Tags", sort.getSortType());
    }

    @Test
    public void testValidSortOrders() {
        sort.setSortOrder("Ascending");
        assertEquals("Ascending", sort.getSortOrder());

        sort.setSortOrder("Descending");
        assertEquals("Descending", sort.getSortOrder());
    }
}
