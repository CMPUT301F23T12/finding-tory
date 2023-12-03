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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This fragment provides a user interface to filter data based on specified criteria such as date range,
 * description, and make. It also supports interaction through a listener pattern to communicate
 * user actions back to the host activity or fragment.
 * <p>
 * The FilterDialogListener interface needs to be implemented by the host to receive callback events
 * for when the filter is confirmed or the dialog is dismissed.
 */
public class FilterFragment extends DialogFragment {
    TextView selectedDate;
    EditText filteredDescription;
    EditText filteredMake;
    Button datePicker;
    Date dateStart;
    Date dateEnd;
    String filteredMakeTxt;
    String filteredDescriptionTxt;
    private FilterFragment.FilterDialogListener listener;

    public interface FilterDialogListener {
        void onFilterDismissed();

        void resetFilter();

        void onFilterConfirmed(Date filterStartDate, Date filterEndDate, String filterDescription, String filterMake);
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

        if (dateStart != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            selectedDate.setText(sdf.format(dateStart) + " - " + sdf.format(dateEnd));
        }
        if (filteredDescriptionTxt != null) {
            filteredDescription.setText(filteredDescriptionTxt);
        }
        if (filteredMakeTxt != null) {
            filteredMake.setText(filteredMakeTxt);
        }

        datePicker.setOnClickListener(v -> DatePickerDialog());
        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnResetFilter).setOnClickListener(v -> {
            if (listener != null) {
                listener.resetFilter();
                selectedDate.setText("MMM DD, YYYY");
                dateStart = null;
                dateEnd = null;
                filteredDescription.setText("");
                filteredMake.setText("");
            }
        });

        view.findViewById(R.id.btnFilter).setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterConfirmed(dateStart, dateEnd, filteredDescription.getText().toString(), filteredMake.getText().toString());
            }
            dismiss();
        });

        return builder.create();
    }

    public void populateFilterParams(Date filterStartDate, Date filterEndDate, String filterDescription, String filterMake) {
        this.dateStart = filterStartDate;
        this.dateEnd = filterEndDate;
        this.filteredMakeTxt = filterMake;
        this.filteredDescriptionTxt = filterDescription;
    }

    /**
     * Sets the listener for the filter dialog interactions.
     *
     * @param listener The FilterFragment.FilterDialogListener instance that will handle dialog callbacks.
     */
    public void setFilterDialogListener(FilterFragment.FilterDialogListener listener) {
        this.listener = listener;
    }

    /**
     * Initializes and displays a MaterialDatePicker dialog for selecting a date range.
     * The selected dates are formatted and displayed in a TextView.
     */
    private void DatePickerDialog() {
        // Creating a MaterialDatePicker builder for selecting a date range
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

//        if (dateStart != null) {
//            builder.setSelection(new Pair<>(dateStart.getTime(), dateEnd.getTime()));
//        }

        // Building the date picker dialog
        MaterialDatePicker<Pair<Long, Long>> datePickerFrag = builder.build();
        datePickerFrag.addOnPositiveButtonClickListener(selection -> {

            // Retrieving the selected start and end dates
            Calendar calendar = Calendar.getInstance();

            // Setting the start date to 00:00:00
            dateStart = new Date(selection.first);
            calendar.setTime(dateStart);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            dateStart = calendar.getTime();

            // Setting the end date to 00:00:00
            dateEnd = new Date(selection.second);
            calendar.setTime(dateEnd);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);
            dateEnd = calendar.getTime();

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
        datePickerFrag.show(getParentFragmentManager(), "DATE_PICKER");
    }

    /**
     * Handles the dismissal of the dialog interface.
     *
     * @param dialog The dialog interface that was dismissed.
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onFilterDismissed();
        }
    }
}
