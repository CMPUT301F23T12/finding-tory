package com.example.finding_tory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    EditText filteredMake;
    Button datePicker;
    private FilterFragment.FilterDialogListener listener;
    private RecyclerView tagsRecyclerView;
    private TagAdapter tagAdapter;
    private ArrayList<String> allTags;
    private Filter filter;

    public interface FilterDialogListener {
        void onFilterDismissed();

        void onFilterConfirmed(Filter filter);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Pass null as the parent view because its going in the dialog layout
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_filter, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        selectedDate = view.findViewById(R.id.selectedDate);
        datePicker = view.findViewById(R.id.datePicker);
        filteredMake = view.findViewById(R.id.editTextMake);

        tagsRecyclerView = (RecyclerView) view.findViewById(R.id.tagsRecyclerView);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        tagsRecyclerView.setLayoutManager(layoutManager);

        tagAdapter = new TagAdapter(allTags, filter.getTags(), false);
        tagsRecyclerView.setAdapter(tagAdapter);

        if (filter.getStartDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            selectedDate.setText(sdf.format(filter.getStartDate()) + " - " + sdf.format(filter.getEndDate()));
        }
        if (filter.getMake() != null) {
            filteredMake.setText(filter.getMake());
        }

        datePicker.setOnClickListener(v -> DatePickerDialog());
        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnResetFilter).setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterConfirmed(new Filter());
            }
            dismiss();
        });

        view.findViewById(R.id.btnFilter).setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterConfirmed(new Filter(filter.getStartDate(), filter.getEndDate(),
                        filter.getDescription(), filteredMake.getText().toString(), filter.getTags()));
            }
            dismiss();
        });

        return dialog;
    }

    /**
     * Sets the parameters for a filter based on various attributes. It assigns the start and end dates for filtering,
     * along with specific attributes such as make and description.
     *
     * @param filter    Any previously-existing filter object, which will be use to pre-fill the dialog.
     * @param allTags   An ArrayList of Strings representing all available tags.
     */
    public void populateFilterParams(Filter filter, ArrayList<String> allTags) {
        this.filter = filter;
        this.allTags = allTags;
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

        // Building the date picker dialog
        MaterialDatePicker<Pair<Long, Long>> datePickerFrag = builder.build();
        datePickerFrag.addOnPositiveButtonClickListener(selection -> {

            // Retrieving the selected start and end dates
            Calendar calendar = Calendar.getInstance();

            // Setting the start date to 00:00:00
            filter.setStartDate(new Date(selection.first));
            calendar.setTime(filter.getStartDate());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            filter.setStartDate(calendar.getTime());

            // Setting the end date to 00:00:00
            filter.setEndDate(new Date(selection.second));
            calendar.setTime(filter.getEndDate());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);
            filter.setEndDate(calendar.getTime());

            // Formatting the selected dates as strings
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String startDateString = sdf.format(filter.getStartDate());
            String endDateString = sdf.format(filter.getEndDate());

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
