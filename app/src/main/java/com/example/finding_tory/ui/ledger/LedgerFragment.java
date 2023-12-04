package com.example.finding_tory.ui.ledger;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finding_tory.ActivityCodes;
import com.example.finding_tory.FirestoreDB;
import com.example.finding_tory.Inventory;
import com.example.finding_tory.InventoryViewActivity;
import com.example.finding_tory.Ledger;
import com.example.finding_tory.LedgerAdapter;
import com.example.finding_tory.R;
import com.example.finding_tory.UpsertInventoryViewActivity;
import com.example.finding_tory.databinding.FragmentLedgerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


/**
 * Displays the Ledger: a list of all the signed-in user's Inventories.
 * Serves as the homepage after signing in.
 */
public class LedgerFragment extends Fragment {

    private FragmentLedgerBinding binding;

    // TODO use a Ledger instead of an ArrayList of Inventories
    private Ledger ledger = Ledger.getInstance();
    private String username;
    private ListView ledgerListView;
    private LedgerAdapter ledgerAdapter;
    private View root;
    private FloatingActionButton addInvButton;

    public static LedgerFragment newInstance(String usrname) {
        LedgerFragment fragment = new LedgerFragment();
        Bundle args = new Bundle();
        args.putSerializable("username", usrname);
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
        ledgerAdapter = new LedgerAdapter(root.getContext(), ledger.getInventories());
        ledgerListView.setAdapter(ledgerAdapter);

        // Retrieve from fragment arguments
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        // cache the add button
        addInvButton = binding.addInventoryButton;

        // launch a new InventoryViewActivity when any of the listed inventories are clicked
        ledgerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InventoryViewActivity.class);
                intent.putExtra("inventory", ledger.getInventories().get(position));
                intent.putExtra("username", username);
                startActivityForResult(intent, 1);
                // getActivity().startActivity(intent);  // launch the InventoryViewActivity
            }
        });

        ledgerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UpsertInventoryViewActivity.class);
                intent.putExtra("inventory", ledger.getInventories().get(position));
                intent.putExtra("username", username);
                startActivityForResult(intent, ActivityCodes.DELETE_INVENTORY.getRequestCode());
                return true;
            }
        });

        // allow new inventories to be added
        binding.addInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addInventoryIntent = new Intent(getActivity(), UpsertInventoryViewActivity.class);
                addInventoryIntent.putExtra("username", username);
                startActivityForResult(addInventoryIntent, ActivityCodes.ADD_INVENTORY.getRequestCode());
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

//    /**
//     * Called when an activity launched by this fragment returns a result.
//     *
//     * @param requestCode The integer request code originally supplied to startActivityForResult().
//     * @param resultCode  The integer result code returned by the child activity.
//     * @param data        An Intent that carries the result data.
//     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // adding new item to list once user submits new item
//        if (requestCode == ActivityCodes.ADD_INVENTORY.getRequestCode()) {
//            if (resultCode == RESULT_OK) {
//                assert data != null;
//                Inventory selectedInventory = (Inventory) data.getSerializableExtra("inventory_to_add");
//
//                assert selectedInventory != null;
//                ledger.getInventories().add(selectedInventory);
//            }
//        } else if (requestCode == ActivityCodes.DELETE_INVENTORY.getRequestCode()) {
//            if (resultCode == RESULT_OK) {
//                assert data != null;
//                Inventory deleteInventory = (Inventory) data.getSerializableExtra("inventory_to_delete");
//                if (deleteInventory != null) {
//                    ledger.deleteInventory(deleteInventory);
//                }
//            }
//        }
//        ledgerAdapter.notifyDataSetChanged();
//    }

    /**
     * When this fragment is first launched or resumed after going back from
     * InventoryViewActivity it updates the inventories for any changes
     */
    @Override
    public void onResume() {
        super.onResume();
        if (username != null && !username.equals("")) {
            fetchUserInventories(); // Refresh data when the fragment becomes visible
        }
    }

    /**
     * Fetches the user's inventories from Firestore and updates the UI with the retrieved data.
     */
    private void fetchUserInventories() {
        ledger.setInventories(new ArrayList<>());
        FirestoreDB.getInventoriesRef(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Add the inventories to the ledger view
                    Inventory inv = document.toObject(Inventory.class);
                    ledger.getInventories().add(inv);
                }
                ledgerAdapter = new LedgerAdapter(root.getContext(), ledger.getInventories());
                ledgerListView.setAdapter(ledgerAdapter);
            } else {
                // TODO Handle the error
            }
        });
    }
}
