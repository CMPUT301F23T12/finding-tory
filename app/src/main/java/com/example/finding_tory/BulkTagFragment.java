package com.example.finding_tory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BulkTagFragment extends DialogFragment {
    private TagDialogListener listener;
    private RecyclerView tagsRecyclerView;
    private TagAdapter tagAdapter;
    private ArrayList<String> allTags; // This should be populated with inventory.getTags()
    private ArrayList<String> selectedTags = new ArrayList<>();

    /**
     * Interface for communicating the dialog actions to the parent context.
     */
    public interface TagDialogListener {
        void onDialogDismissed();

        void onTagConfirmed(ArrayList<String> selectedTags);
    }

    /**
     * Sets the listener that will handle dialog actions.
     * Call this method to set the listener in your fragment.
     *
     * @param listener The listener that will be notified of dialog actions.
     */
    public void setTagDialogListener(TagDialogListener listener) {
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
        View view = requireActivity().getLayoutInflater().inflate(R.layout.fragment_bulk_tags, null);
        builder.setView(view);

        Inventory inventory = null;
        if (getArguments() != null) {
            inventory = (Inventory) getArguments().getSerializable("inventory");
        }

        // Initialize RecyclerView and Adapter
        tagsRecyclerView = view.findViewById(R.id.tagsRecyclerView);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allTags = inventory.getTags();
        selectedTags = new ArrayList<>((Set<String>) getArguments().getSerializable("tags"));

        tagAdapter = new TagAdapter(allTags, selectedTags);
        tagsRecyclerView.setAdapter(tagAdapter);
        // Add action buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnAdd).setOnClickListener(v -> {
            if (listener != null) {
                listener.onTagConfirmed(selectedTags); // Notify the listener

            }
            dismiss();
        });

        return builder.create();
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
