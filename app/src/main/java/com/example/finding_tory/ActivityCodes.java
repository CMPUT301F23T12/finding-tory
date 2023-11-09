package com.example.finding_tory;

public enum ActivityCodes {
    ADD_ITEM(0), EDIT_ITEM(1), DELETE_ITEM(2), CANCEL_ITEM(3), VIEW_ITEM(4);

    private int requestCode;
    ActivityCodes(int code) {
        this.requestCode = code;
    }
    public int getRequestCode() {
        return requestCode;
    }
}
