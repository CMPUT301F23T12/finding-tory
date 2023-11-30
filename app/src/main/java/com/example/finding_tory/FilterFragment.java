package com.example.finding_tory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FilterFragment extends DialogFragment {
    TextView selectedDate;
    EditText filteredDescription;
    EditText filteredMake;
    Button datePicker;
    Date dateStart;
    Date dateEnd;

    public interface FilterDialogListener {
        void onFilterDismissed();

        void onFilterConfirmed(Date filterStartDate, Date filterEndDate, String filterDescription, String filterMake);
    }

    private FilterFragment.FilterDialogListener listener;

    public void setFilterDialogListener(FilterFragment.FilterDialogListener listener) {
        this.listener = listener;
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Pass null as the parent view because its going in the dialog layout
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_filter, null);
        builder.setView(view);

        selectedDate = view.findViewById(R.id.selectedDate);
        datePicker = view.findViewById(R.id.datePicker);
        filteredMake = view.findViewById(R.id.editTextMake);
        filteredDescription = view.findViewById(R.id.editTextDescription);

        datePicker.setOnClickListener(v -> DatePickerDialog());
        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnFilter).setOnClickListener(v -> {

            if (listener != null) {
                listener.onFilterConfirmed(dateStart, dateEnd, filteredDescription.getText().toString(), filteredMake.getText().toString());
            }
            dismiss();
        });

        return builder.create();
    }

    private void DatePickerDialog() {
        // Creating a MaterialDatePicker builder for selecting a date range
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

        // Building the date picker dialog
        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {

            // Retrieving the selected start and end dates
            dateStart = new Date(selection.first);
            dateEnd = new Date(selection.second);

            // Formatting the selected dates as strings
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String startDateString = sdf.format(dateStart);
            String endDateString = sdf.format(dateEnd);

            // Creating the date range string
            String selectedDateRange = startDateString + " - " + endDateString;

            // Displaying the selected date range in the TextView
            selectedDate.setText(selectedDateRange);
        });

        // Showing the date picker dialog
        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onFilterDismissed();
        }
    }
}
