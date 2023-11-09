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
    }

    private DeleteDialogListener listener;

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
            dismiss();
        });

        return builder.create();
    }

    // Call this method to set the listener in your fragment
    public void setDeleteDialogListener(DeleteDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDialogDismissed();
        }
    }
}
