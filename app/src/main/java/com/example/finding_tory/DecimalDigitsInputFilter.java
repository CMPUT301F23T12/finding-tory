package com.example.finding_tory;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext was used to find this
// class to limit the amount of decimals a number input can have (for charge input)
class DecimalDigitsInputFilter implements InputFilter {
    private final Pattern mPattern;
    DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}
