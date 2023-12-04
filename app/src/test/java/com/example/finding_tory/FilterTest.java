
package com.example.finding_tory;

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

public class FilterTest extends TestCase {
    private Filter filter;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    public void setUp() {
        filter = new Filter();
        startDate = new Date(2021, 1, 1); // Note: This constructor is deprecated, used for simplicity here.
        endDate = new Date(2021, 12, 31); // Similarly, this is for example purposes.
    }

    @Test
    public void testDefaultConstructor() {
        assertTrue(filter.getDescription().isEmpty());
        assertTrue(filter.getMake().isEmpty());
        assertTrue(filter.getTags().isEmpty());
        assertNull(filter.getStartDate());
        assertNull(filter.getEndDate());
        assertTrue(filter.isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Electronics");
        Filter customFilter = new Filter(startDate, endDate, "Laptop", "Dell", tags);

        assertEquals(startDate, customFilter.getStartDate());
        assertEquals(endDate, customFilter.getEndDate());
        assertEquals("Laptop", customFilter.getDescription());
        assertEquals("Dell", customFilter.getMake());
        assertEquals(tags, customFilter.getTags());
        assertFalse(customFilter.isEmpty());
    }

    @Test
    public void testSetAndGetStartDate() {
        filter.setStartDate(startDate);
        assertEquals(startDate, filter.getStartDate());
    }

    @Test
    public void testSetAndGetEndDate() {
        filter.setEndDate(endDate);
        assertEquals(endDate, filter.getEndDate());
    }

    @Test
    public void testSetAndGetDescription() {
        filter.setDescription("Phone");
        assertEquals("Phone", filter.getDescription());
    }

    @Test
    public void testSetAndGetMake() {
        filter.setMake("Apple");
        assertEquals("Apple", filter.getMake());
    }

    @Test
    public void testSetAndGetTags() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Technology");
        filter.setTags(tags);
        assertEquals(tags, filter.getTags());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(filter.isEmpty());

        filter.setDescription("Camera");
        assertFalse(filter.isEmpty());

        // Reset and test with another field
        filter = new Filter();
        filter.setTags(new ArrayList<String>() {{
            add("Photo");
        }});
        assertFalse(filter.isEmpty());
    }
}
