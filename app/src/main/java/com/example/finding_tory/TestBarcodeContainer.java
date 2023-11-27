package com.example.finding_tory;

import java.util.ArrayList;

/**
 * A container class to hold barcodes that will be used for testing
 */
public class TestBarcodeContainer {
    public static ArrayList<Barcode> barcodes = new ArrayList<>();

    /**
     * Constructs a TestBarcodeContainer with all test barcodes data added
     */
    public TestBarcodeContainer() {
        barcodes.add(new Barcode("012345678905", "Headphones", "Airpods Pro", "Gen 2", "329.99"));
        barcodes.add(new Barcode("8480000330451", "Toaster", "Hamilton Beach", "Classic", "24.99"));
        barcodes.add(new Barcode("0123456789012", "Bed", "IKEA", "Malm", "399.99"));
    }

    /**
     * Checks if product number scanned is equal to the barcode saved
     *
     * @param product_number      the product number scanned from barcode
     * @return barcode data if product number is in test list
     */
    public Barcode getBarcodeInfo(String product_number) {
        for (Barcode barcode : barcodes) {
            if (barcode.equals(product_number)) {
                return barcode;
            }
        }
        return null;
    }
}
