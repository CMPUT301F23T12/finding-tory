package com.example.finding_tory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteItemFragment extends DialogFragment {
    public interface DeleteDialogListener {
        void onDialogDismissed();
        void onDeleteConfirmed();
    }

    private DeleteDialogListener listener;

    // Call this method to set the listener in your fragment
    public void setDeleteDialogListener(DeleteDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Pass null as the parent view because its going in the dialog layout
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_confirmation, null);
        builder.setView(view);

        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            // TODO: Implement delete logic
            if (listener != null) {
                listener.onDeleteConfirmed(); // Notify the listener
            }
            dismiss();
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
