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
import java.util.List;


/**
 * Displays the Ledger: a list of all the signed-in user's Inventories.
 *  Serves as the homepage after signing in.
 */
public class LedgerFragment extends Fragment {

    private FragmentLedgerBinding binding;

    // TODO use a Ledger instead of an ArrayList of Inventories
    private ArrayList<Inventory> inventories = new ArrayList<>();
    private ListView ledgerListView;
    private LedgerAdapter ledgerAdapter;

    private FloatingActionButton addInvButton;

    public static LedgerFragment newInstance(String username) {
        LedgerFragment fragment = new LedgerFragment();
        Bundle args = new Bundle();
        System.out.println("LF: " + username);
        args.putSerializable("username", username); // Use putSerializable for User
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Maps the ledger adapter to the underlying list of inventories, and sets
     *  any click listeners for each list element/the add button.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view that is created.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLedgerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // create a mock inventory with mock items to populate the list


//        Inventory mockInventory = new Inventory("Home Inventory");
//        inventories = new ArrayList<>();

//        User user = (User) getIntent().getSerializableExtra("user");
//        fetchUserInventories();
//        inventories.add(mockInventory);
//
//        // Populate the view with retrieved items from the db
//        FirestoreDB.getItemsRef().get().addOnSuccessListener(queryDocumentSnapshots -> {
//            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                Item item = documentSnapshot.toObject(Item.class);
//                if (item != null) {
//                    mockInventory.addItem(item);
//                }
//            }
//            // need to notify because this block may or may not finish before the next block.
//            // since it's a "success listener", we have no idea when this code gets executed.
//            ledgerAdapter.notifyDataSetChanged();
//        });

        // map the listview to the ledger's list of items via custom ledger adapter
        ledgerListView = binding.ledgerListview;
        ledgerAdapter = new LedgerAdapter(root.getContext(), inventories);
        ledgerListView.setAdapter(ledgerAdapter);

        // Retrieve from fragment arguments
        if (getArguments() != null) {
            String username = getArguments().getString("username");
            System.out.println("LF2: " + username);
            if (username != null) {
                FirestoreDB.getInventoriesRef(username).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("LF3: " + username);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming you have a constructor in your Inventory class that can create an instance from a DocumentSnapshot
                            Inventory inventory = document.toObject(Inventory.class);


                            inventories.add(inventory);
                            ledgerAdapter.notifyDataSetChanged();
                            System.out.println(document.getData());
                            System.out.println(inventory.getInventoryName());
                        }
                        ledgerAdapter = new LedgerAdapter(root.getContext(), inventories);
                        ledgerListView.setAdapter(ledgerAdapter);
                        // Do something with the list of inventories
                    } else {
                        // Handle the error
                    }
                });
            }
        }



        // cache the add button
        addInvButton = binding.addInventoryButton;

        // launch a new InventoryViewActivity when any of the listed inventories are clicked
        ledgerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InventoryViewActivity.class);
                intent.putExtra("inventory", inventories.get(position));
                getActivity().startActivity(intent);  // launch the InventoryViewActivity
            }
        });

        // allow new inventories to be added
        binding.addInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO allow creation of new, unrelated inventories
                Snackbar.make(view, "Create an inventory (Coming soon!)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return root;
    }

//    private void fetchUserInventories() {
//        // Replace with your FirestoreDB logic to fetch user inventories
//        FirestoreDB.getInventoriesRef().get().addOnSuccessListener(queryDocumentSnapshots -> {
//            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                Inventory inventory = documentSnapshot.toObject(Inventory.class);
//                if (inventory != null) {
//                    inventories.add(inventory);
//                }
//            }
//            // Notify the adapter that data has changed
//            ledgerAdapter.notifyDataSetChanged();
//        }).addOnFailureListener(e -> {
//            // Handle any errors that occur during the fetch
//            // You may want to log or display an error message
//        });
//    }

    /**
     * Cleanup when this fragment's life cycle is over.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchUserInventories(String username) {
        // Assuming FirestoreDB.getUsersRef() gives you a CollectionReference to the 'users' collection
        FirestoreDB.getUsersRef().document(username).collection("inventories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    inventories.clear(); // Clear existing data to avoid duplicates
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Inventory inventory = documentSnapshot.toObject(Inventory.class);
                        if (inventory != null) {
                            inventories.add(inventory);
                            System.out.println(inventory.getInventoryName());
                        }
                    }
                    // Notify the adapter that data has changed
                    ledgerAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occur during the fetch
                    // You may want to log or display an error message
                });
    }

}
