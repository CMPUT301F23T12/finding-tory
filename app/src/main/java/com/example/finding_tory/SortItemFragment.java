package com.example.finding_tory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Date;

/**
 * SortItemFragment is a DialogFragment that allows users to choose sorting criteria.
 * It provides a customizable dialog with sorting options, and it communicates the user's
 * choice back to the parent context through the SortDialogListener interface.
 */
public class SortItemFragment extends DialogFragment {
    /**
     * The interface for event handling from the SortItemFragment.
     */
    public interface SortDialogListener {
        void onDialogDismissed();

        void onSortConfirmed(String sort_type, String sort_order);
    }

    private SortItemFragment.SortDialogListener listener;
    private int i1 = -1, i2 = -1;

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

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnSort).setOnClickListener(v -> {
            RadioGroup radioGroup1 = view.findViewById(R.id.radioGroup1);
            RadioGroup radioGroup2 = view.findViewById(R.id.radioGroup2);
            i1 = radioGroup1.getCheckedRadioButtonId();
            i2 = radioGroup2.getCheckedRadioButtonId();

            if (listener != null) {
                if (i1 != -1 && i2 != -1) {
                    RadioButton selectedButton1 = view.findViewById(i1);
                    RadioButton selectedButton2 = view.findViewById(i2);

                    listener.onSortConfirmed(selectedButton1.getText().toString(), selectedButton2.getText().toString());
                    dismiss();
                }
            } else {
                dismiss();
            }
        });

        return dialog;
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
