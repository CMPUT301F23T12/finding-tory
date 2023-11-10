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

public class SortItemFragment extends DialogFragment {
    public interface SortDialogListener {
        void onDialogDismissed();

        void onSortConfirmed(String sort_type, String sort_order);
    }

    private SortItemFragment.SortDialogListener listener;
    private int i1 = -1, i2 = -1;

    public void setSortDialogListener(SortItemFragment.SortDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Pass null as the parent view because its going in the dialog layout
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_sort, null);
        builder.setView(view);

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

        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDialogDismissed();
        }
    }
}
