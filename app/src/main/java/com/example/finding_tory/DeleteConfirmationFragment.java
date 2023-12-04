package com.example.finding_tory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * DeleteConfirmationFragment is a DialogFragment subclass used for confirming item deletion in the application.
 * It provides a dialog interface to the user for confirming or canceling the deletion of an item.
 * <p>
 * The fragment uses an AlertDialog to present the options to the user and communicates the user's actions
 * back to the parent context using a custom listener interface.
 */
public class DeleteConfirmationFragment extends DialogFragment {
    private DeleteDialogListener listener;

    /**
     * Interface for communicating the dialog actions to the parent context.
     */
    public interface DeleteDialogListener {
        void onDialogDismissed();

        void onDeleteConfirmed();
    }

    /**
     * Sets the listener that will handle dialog actions.
     * Call this method to set the listener in your fragment.
     *
     * @param listener The listener that will be notified of dialog actions.
     */
    public void setDeleteDialogListener(DeleteDialogListener listener) {
        this.listener = listener;
    }

    /**
     * Creates the dialog interface for item deletion confirmation.
     * Sets up the view and handles button clicks for confirming or canceling the deletion.
     *
     * @param savedInstanceState If non-null, this dialog is being re-constructed from a previous saved state.
     * @return The newly created dialog.
     */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Pass null as the parent view because its going in the dialog layout
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_confirmation, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteConfirmed(); // Notify the listener
            }
            dismiss();
        });

        return dialog;
    }


    /**
     * Called when the dialog is dismissed.
     * Notifies the listener that the dialog has been dismissed.
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
