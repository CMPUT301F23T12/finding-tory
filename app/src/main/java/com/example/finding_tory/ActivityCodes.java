package com.example.finding_tory;

/**
 * Enum representing various activity request codes used in the application.
 * Each constant in this enum corresponds to a specific operation or action related to item management.
 * This enum provides a standardized way of identifying and handling different activity requests.
 */
public enum ActivityCodes {
    ADD_ITEM(0), EDIT_ITEM(1), DELETE_ITEM(2), CANCEL_ITEM(3), VIEW_ITEM(4), CAMERA_PHOTO(5), GALLERY_PHOTO(6), ADD_INVENTORY(7), DELETE_INVENTORY(8), SCAN_SERIAL_NUMBER(9), REGISTER_USER(10), LOGIN_USER(11);

    private int requestCode;

    /**
     * Constructor for ActivityCodes enum.
     * Initializes the enum with a specific request code.
     *
     * @param code The request code associated with the activity action.
     */
    ActivityCodes(int code) {
        this.requestCode = code;
    }

    /**
     * Retrieves the request code associated with the activity action.
     *
     * @return The integer request code.
     */
    public int getRequestCode() {
        return requestCode;
    }
}
