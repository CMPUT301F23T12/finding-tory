package com.example.finding_tory;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DecimalDigitsInputFilter is an implementation of InputFilter that restricts the number of digits before and after
 * the decimal point in numerical input. This class is particularly useful for fields where precise decimal values are required,
 * such as monetary amounts.
 * <p>
 * The class uses regular expressions to enforce the input constraints.
 */
class DecimalDigitsInputFilter implements InputFilter {
    // https://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext was used to find this
// class to limit the amount of decimals a number input can have (for charge input)
    private final Pattern mPattern;

    /**
     * Constructor for DecimalDigitsInputFilter.
     * Initializes the filter with specific limits for the number of digits before and after the decimal point.
     *
     * @param digitsBeforeZero The maximum number of digits allowed before the decimal point.
     * @param digitsAfterZero  The maximum number of digits allowed after the decimal point.
     */
    DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
    }

    @Override

    /**
     * Filters the input based on the defined pattern. If the input does not match the pattern, it restricts the input.
     *
     * @param source The input sequence of characters.
     * @param start  The starting index of the range of text to be replaced in the original text.
     * @param end    The ending index of the range of text to be replaced in the original text.
     * @param dest   The original text to be modified.
     * @param dstart The starting index in dest of the range of the original text to be replaced.
     * @param dend   The ending index in dest of the range of the original text to be replaced.
     * @return The modified CharSequence to replace the original input, or null to accept the original input.
     */ public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches()) return "";
        return null;
    }
}
