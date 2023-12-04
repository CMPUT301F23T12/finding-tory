package com.example.finding_tory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * SortItemFragment is a DialogFragment that allows users to choose sorting criteria.
 * It provides a customizable dialog with sorting options, and it communicates the user's
 * choice back to the parent context through the SortDialogListener interface.
 */
public class SortItemFragment extends DialogFragment {
    private SortItemFragment.SortDialogListener listener;
    private RadioGroup sortTypeRadioGroup;
    private RadioGroup sortOrderRadioGroup;
    private int typeID = -1, orderID = -1;
    private Sort sort;

    /**
     * The interface for event handling from the SortItemFragment.
     */
    public interface SortDialogListener {
        void onDialogDismissed();

        void onSortConfirmed(Sort sort);
    }

    /**
     * Sets the listener that will handle events from this dialog.
     *
     * @param listener The SortDialogListener to set.
     */
    public void setSortDialogListener(SortItemFragment.SortDialogListener listener) {
        this.listener = listener;
    }

    /**
     * Creates the sorting dialog with the specified options.
     *
     * @param savedInstanceState If the dialog is being re-initialized after previously
     *                           being shut down, this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     * @return A new dialog instance to be displayed by the Fragment.
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Pass null as the parent view because its going in the dialog layout
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_sort, null);
        builder.setView(view);

        // initialize the RadioGroups in the fragment
        sortTypeRadioGroup = view.findViewById(R.id.radioGroup1);
        sortOrderRadioGroup = view.findViewById(R.id.radioGroup2);

        // prefill the radio buttons based on the existing sort
        RadioButton selectedType, selectedOrder;
        switch (sort.getSortType()) {
            case "Date":
                selectedType = view.findViewById(R.id.radio_Date);
                break;
            case "Make":
                selectedType = view.findViewById(R.id.radio_Make);
                break;
            case "Value":
                selectedType = view.findViewById(R.id.radio_Value);
                break;
            case "Tags":
                selectedType = view.findViewById(R.id.radio_Tags);
                break;
            default:
                selectedType = view.findViewById(R.id.radio_Description);
        }
        if (sort.getSortOrder().equals("Ascending")) {
            selectedOrder = view.findViewById(R.id.radio_ascending);
        } else {
            selectedOrder = view.findViewById(R.id.radio_descending);
        }
        selectedType.setChecked(true);
        selectedOrder.setChecked(true);

        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnSort).setOnClickListener(v -> {
            typeID = sortTypeRadioGroup.getCheckedRadioButtonId();
            orderID = sortOrderRadioGroup.getCheckedRadioButtonId();

            if (listener != null) {
                if (typeID != -1 && orderID != -1) {
                    RadioButton selectedButton1 = view.findViewById(typeID);
                    RadioButton selectedButton2 = view.findViewById(orderID);

                    listener.onSortConfirmed(new Sort(selectedButton1.getText().toString(),
                            selectedButton2.getText().toString()));
                    dismiss();
                }
            } else {
                dismiss();
            }
        });

        return builder.create();
    }

    // TODO create javadoc
    public void setSortParams(Sort sort) {
        this.sort = sort;
    }

    /**
     * Called when the dialog is closed.
     *
     * @param dialog The dialog that was dismissed.
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDialogDismissed();
        }
    }
}
