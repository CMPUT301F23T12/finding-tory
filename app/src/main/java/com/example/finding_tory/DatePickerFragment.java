package com.example.finding_tory;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class is responsible for creating the date picker dialog
 */
public class DatePickerFragment extends DialogFragment {
    /**
     * Initializes the fragment for use to pick a date from a calendar
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        Calendar calendar = Calendar.getInstance();

        // if date input is set in parent, will initialize it to that date
        if (bundle.containsKey("date")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                calendar.setTime(sdf.parse(bundle.getString("date")));
            } catch (ParseException e) {
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // creates the fragment to pick date
        //TODO: Fix color of header background
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, dayOfMonth);
    }
}