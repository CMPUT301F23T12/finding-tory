package com.example.finding_tory.ui.ledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finding_tory.FirestoreDB;
import com.example.finding_tory.Inventory;
import com.example.finding_tory.InventoryViewActivity;
import com.example.finding_tory.LedgerAdapter;
import com.example.finding_tory.databinding.FragmentLedgerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


/**
 * Displays the Ledger: a list of all the signed-in user's Inventories.
 * Serves as the homepage after signing in.
 */
public class LedgerFragment extends Fragment {

    private FragmentLedgerBinding binding;

    // TODO use a Ledger instead of an ArrayList of Inventories
    private ArrayList<Inventory> inventories = new ArrayList<>();
    private String username;
    private ListView ledgerListView;
    private LedgerAdapter ledgerAdapter;
    private View root;
    private FloatingActionButton addInvButton;

    public static LedgerFragment newInstance(String username) {
        LedgerFragment fragment = new LedgerFragment();
        Bundle args = new Bundle();
        args.putSerializable("username", username); // Use putSerializable for User
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Maps the ledger adapter to the underlying list of inventories, and sets
     * any click listeners for each list element/the add button.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return the view that is created.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLedgerBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        // map the listview to the ledger's list of items via custom ledger adapter
        ledgerListView = binding.ledgerListview;
        ledgerAdapter = new LedgerAdapter(root.getContext(), inventories);
        ledgerListView.setAdapter(ledgerAdapter);

        // Retrieve from fragment arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
            if (username != null) {
                fetchUserInventories();
            }
        }

        // cache the add button
        addInvButton = binding.addInventoryButton;

        // launch a new InventoryViewActivity when any of the listed inventories are clicked
        ledgerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InventoryViewActivity.class);
                intent.putExtra("inventoryName", inventories.get(position).getInventoryName());
                intent.putExtra("username", username);
                startActivityForResult(intent, 1);
                // getActivity().startActivity(intent);  // launch the InventoryViewActivity
            }
        });

        // allow new inventories to be added
        binding.addInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO allow creation of new, unrelated inventories
                Snackbar.make(view, "Create an inventory (Coming soon!)", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return root;
    }

    /**
     * Cleanup when this fragment's life cycle is over.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            fetchUserInventories();
            ledgerAdapter.notifyDataSetChanged();
        }
    }

    private void fetchUserInventories() {
        inventories = new ArrayList<>();
        FirestoreDB.getInventoriesRef(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Add the inventories
                    Inventory inv = document.toObject(Inventory.class);
                    inventories.add(inv);
                }
                ledgerAdapter = new LedgerAdapter(root.getContext(), inventories);
                ledgerListView.setAdapter(ledgerAdapter);
            } else {
                // TODO Handle the error
            }
        });
    }
}
