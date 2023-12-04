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
    EditText filteredDescription;
    EditText filteredMake;
    Button datePicker;
    Date dateStart;
    Date dateEnd;
    String filteredMakeTxt;
    String filteredDescriptionTxt;
    private FilterFragment.FilterDialogListener listener;
    private RecyclerView tagsRecyclerView;
    private TagAdapter tagAdapter;
    private ArrayList<String> allTags;
    private ArrayList<String> selectedTags = new ArrayList<>();

    public interface FilterDialogListener {
        void onFilterDismissed();

        void onFilterConfirmed(Date filterStartDate, Date filterEndDate, String filterDescription, String filterMake, ArrayList<String> filterTags);
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

        tagsRecyclerView = (RecyclerView) view.findViewById(R.id.tagsRecyclerView);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        tagsRecyclerView.setLayoutManager(layoutManager);

        tagAdapter = new TagAdapter(allTags, selectedTags, false);
        tagsRecyclerView.setAdapter(tagAdapter);

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
                listener.onFilterConfirmed(null, null, "", "", new ArrayList<String>());
            }
            dismiss();
        });

        view.findViewById(R.id.btnFilter).setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterConfirmed(dateStart, dateEnd, filteredDescription.getText().toString(), filteredMake.getText().toString(), selectedTags);
            }
            dismiss();
        });

        return builder.create();
    }

    /**
     * Sets the parameters for a filter based on various attributes. It assigns the start and end dates for filtering,
     * along with specific attributes such as make and description.
     *
     * @param filterStartDate   The start date for the filter, used to determine the beginning of the filtering period.
     * @param filterEndDate     The end date for the filter, used to determine the end of the filtering period.
     * @param filterDescription A String representing the description to be used as part of the filter criteria.
     * @param filterMake        A String representing the make (or brand) to be used as part of the filter criteria.
     * @param allPassedInTags   An ArrayList of Strings representing all available tags.
     * @param selectTags        An ArrayList of Strings representing the selected tags to be used in the filter.
     */
    public void populateFilterParams(Date filterStartDate, Date filterEndDate, String filterDescription, String filterMake, ArrayList<String> allPassedInTags, ArrayList<String> selectTags) {
        this.dateStart = filterStartDate;
        this.dateEnd = filterEndDate;
        this.filteredMakeTxt = filterMake;
        this.filteredDescriptionTxt = filterDescription;
        this.allTags = allPassedInTags;
        this.selectedTags = selectTags;
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
