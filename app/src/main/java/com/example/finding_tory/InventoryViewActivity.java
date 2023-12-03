package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;


/**
 * Displays the content of a given Inventory (ie. a list of Items).
 */
public class InventoryViewActivity extends AppCompatActivity {
    private String username;
    private Inventory inventory;
    private ListView inventoryListView;
    private InventoryAdapter inventoryAdapter;
    private boolean state_deletion = false;
    private boolean state_filter = false;
    private TextView totalItemsTextView;
    private TextView totalValueTextView;
    private FloatingActionButton addItemButton;
    private ImageButton filter_tag_button, sort_cancel_button;

    /**
     * Initializes the instance variables and bindings associated with this activity on creation.
     *
     * @param savedInstanceState Possible saved state information for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_view);

        // TODO get an actual inventory from the db
        // get the inventory that's been passed by the ledger view parent activity
        Intent intent = getIntent();
        inventory = (Inventory) intent.getSerializableExtra("inventory");
        username = (String) intent.getSerializableExtra("username");
        inventory.setItems(new ArrayList<>());
        inventory.setFilter(null, null, "", "");

        populateInventoryItems();
        assert (inventory != null);
        setTitle(inventory.getInventoryName());

        // map the listview to the inventory's list of items via custom inventory adapter
        inventoryListView = findViewById(R.id.inventory_listview);
        inventoryAdapter = new InventoryAdapter(this, inventory.getDisplayedItems());
        inventoryListView.setAdapter(inventoryAdapter);

        // initialize and cache the TextViews for the totals
        totalItemsTextView = findViewById(R.id.total_items_textview);
        totalValueTextView = findViewById(R.id.total_value_textview);
        addItemButton = findViewById(R.id.add_delete_item_button);
        updateTotals(false);

        EditText findItemDesc = findViewById(R.id.search_inventory_edittext);
        findItemDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                state_filter = !temp.equals("");
                inventory.setFilter(inventory.filteredStartDate, inventory.filteredEndDate, temp, inventory.filteredMake);
                inventory.filterItems();
                updateTotals(false);
            }
        });

        // allows new items to be added
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state_deletion) {
                    final View greyBack = findViewById(R.id.fadeBackground);
                    DeleteConfirmationFragment deleteDialog = new DeleteConfirmationFragment();
                    deleteDialog.setDeleteDialogListener(new DeleteConfirmationFragment.DeleteDialogListener() {
                        @Override
                        public void onDialogDismissed() {
                            // Make grey background invisible when the dialog is dismissed
                            inventoryAdapter.clearSelection();
                            greyBack.setVisibility(View.GONE);
                            exitSelectionMode();
                        }

                        @Override
                        public void onDeleteConfirmed() {
                            List<Item> selectedItems = inventoryAdapter.getSelectedItems();
                            // Clear the selection and exit selection mode
                            inventoryAdapter.clearSelection();
                            exitSelectionMode();

                            // Remove selected items from the inventory
                            for (Item item : selectedItems) {
                                inventory.removeItem(item);
                                FirestoreDB.deleteItemDB(username, inventory, item);
                            }

                            updateTotals(true);
                        }
                    });
                    deleteDialog.show(getSupportFragmentManager(), "DELETE_ITEM");
                    greyBack.setVisibility(View.VISIBLE); // move to the bottom after filter is implemented
                } else {
                    Intent editItemIntent = new Intent(InventoryViewActivity.this, UpsertViewActivity.class);
                    editItemIntent.putExtra("username", username);
                    editItemIntent.putExtra("inventory", inventory);
                    startActivityForResult(editItemIntent, ActivityCodes.ADD_ITEM.getRequestCode());
                }
            }
        });

        // Handles creating new activity for viewing item
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Item selectedItem = inventoryAdapter.getItem(position);

                Intent intent = new Intent(InventoryViewActivity.this, ItemViewActivity.class);

                intent.putExtra("selectedItem", selectedItem);
                intent.putExtra("pos", position);
                intent.putExtra("inventory", inventory);
                intent.putExtra("username", username);
                startActivityForResult(intent, ActivityCodes.VIEW_ITEM.getRequestCode());
            }
        });

        sort_cancel_button = findViewById(R.id.sort_cancel_button);
        sort_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state_deletion) {
                    inventoryAdapter.clearSelection();
                    exitSelectionMode();
                } else {
                    final View greyBack = findViewById(R.id.fadeBackground);
                    SortItemFragment sortDialog = new SortItemFragment();
                    sortDialog.setSortDialogListener(new SortItemFragment.SortDialogListener() {
                        @Override
                        public void onDialogDismissed() {
                            greyBack.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSortConfirmed(String sort_type, String sort_order) {
                            inventory.setSortData(sort_type, sort_order);
                            if (inventory.sortItems()) {
                                inventoryAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    sortDialog.show(getSupportFragmentManager(), "SORT_ITEM");
                    greyBack.setVisibility(View.VISIBLE);
                }
            }
        });

        filter_tag_button = findViewById(R.id.filter_tag_button);
        filter_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state_deletion) {
                    final View greyBack = findViewById(R.id.fadeBackground);
                    Set<String> current_tags = new HashSet<>();
                    for (Item item : inventoryAdapter.getSelectedItems()) {
                        current_tags.addAll(item.getItemTags());
                    }

                    BulkTagFragment tagDialog = new BulkTagFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("inventory", inventory);
                    args.putSerializable("tags", (Serializable) new ArrayList<>(current_tags));
                    tagDialog.setArguments(args);
                    tagDialog.setTagDialogListener(new BulkTagFragment.TagDialogListener() {
                        @Override
                        public void onDialogDismissed() {
                            greyBack.setVisibility(View.GONE);
                        }

                        @Override
                        public void onTagConfirmed(ArrayList<String> selectedTags) {
                            for (Item item : inventoryAdapter.getSelectedItems()) {
                                for (String str : selectedTags) {
                                    item.addItemTag(str);
                                }
                                FirestoreDB.editItemFromFirestore(username, inventory, item);
                            }
                            inventory.addTagsToInventory(selectedTags);
                            inventoryAdapter.clearSelection();
                            exitSelectionMode();
                            // Notify the adapter of the data change
                            inventoryAdapter.notifyDataSetChanged();
                        }
                    });
                    tagDialog.show(getSupportFragmentManager(), "TAG_ITEMS");
                    greyBack.setVisibility(View.VISIBLE);
                } else {
                    final View greyBack = findViewById(R.id.fadeBackground);
                    FilterFragment filterDialog = new FilterFragment();
                    filterDialog.populateFilterParams(inventory.filteredStartDate, inventory.filteredEndDate, inventory.filteredDescription, inventory.filteredMake);
                    filterDialog.setFilterDialogListener(new FilterFragment.FilterDialogListener() {
                        @Override
                        public void onFilterDismissed() {
                            greyBack.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFilterConfirmed(Date filterStartDate, Date filterEndDate, String filterDescription, String filterMake) {
                            state_filter = filterStartDate != null || filterEndDate != null || !filterDescription.equals("") || !filterMake.equals("");
                            inventory.setFilter(filterStartDate, filterEndDate, filterDescription, filterMake);
                            inventory.filterItems();
                            updateTotals(false);
                        }
                    });
                    filterDialog.show(getSupportFragmentManager(), "FILTER_ITEMS");
                    greyBack.setVisibility(View.VISIBLE);
                }
            }
        });

        inventoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                enterSelectionMode();
                return true;
            }
        });
    }

    /**
     * Enters the selection mode for the inventory items. In selection mode, checkboxes are displayed
     * next to each item in the list, allowing the user to select multiple items for actions like deletion.
     */
    private void enterSelectionMode() {
        state_deletion = true;
        filter_tag_button.setBackgroundResource(R.drawable.tag_png);
        sort_cancel_button.setBackgroundResource(android.R.drawable.ic_delete);
        addItemButton.setImageResource(android.R.drawable.ic_menu_delete);
        // Show checkboxes
        for (int i = 0; i < inventoryAdapter.getCount(); i++) {
            View view = inventoryListView.getChildAt(i);
            CheckBox checkBox = view.findViewById(R.id.item_checkbox);
            ImageView arrow = view.findViewById(R.id.arrow);
            checkBox.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);
        }
    }

    /**
     * Exits the selection mode for the inventory items. In selection mode, checkboxes are displayed
     * next to each item in the list. This method hides the checkboxes and returns the inventory view
     * to its normal state.
     */
    private void exitSelectionMode() {
        state_deletion = false;
        filter_tag_button.setBackgroundResource(R.drawable.filter_png);
        sort_cancel_button.setBackgroundResource(R.drawable.sort_png);
        addItemButton.setImageResource(android.R.drawable.ic_input_add);
        for (int i = 0; i < inventoryAdapter.getCount(); i++) {
            View view = inventoryListView.getChildAt(i);
            CheckBox checkBox = view.findViewById(R.id.item_checkbox);
            ImageView arrow = view.findViewById(R.id.arrow);
            checkBox.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
        }
    }

    /**
     * Handles the result of activities that were started for a result.
     *
     * @param requestCode The request code that was used to start the activity.
     * @param resultCode  The result code returned by the activity.
     * @param data        The data returned by the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // adding new item to list once user submits new item
        if (requestCode == ActivityCodes.ADD_ITEM.getRequestCode()) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Item selectedItem = (Item) data.getSerializableExtra("item_to_add");
                assert selectedItem != null;
                inventory.addItem(selectedItem);
                inventory.sortItems();
                inventoryAdapter.notifyDataSetChanged();
                updateTotals(true);
                inventory.filterItems();
            }
        }

        // updates the item at position passed
        if (requestCode == ActivityCodes.VIEW_ITEM.getRequestCode()) {
            int pos = data.getIntExtra("position", 0);
            if (Objects.equals(data.getStringExtra("action"), "delete")) {
                int position = data.getIntExtra("pos", -1);
                if (position >= 0) {
                    FirestoreDB.deleteItemDB(username, inventory, inventory.getDisplayedItems().get(position));
                    inventory.removeItemByIndex(position);
                }
            } else {
                Item returnedItem = (Item) data.getSerializableExtra("returnedItem");
                inventory.set(pos, returnedItem);
                FirestoreDB.editItemFromFirestore(username, inventory, returnedItem);
                inventory.filterItems();
            }
            updateTotals(true);
        }
    }

    /**
     * Rewrites the TextView elements displaying the inventory totals to reflect new values.
     */
    public void updateTotals(boolean updateDB) {
        if (!state_filter) {
            totalItemsTextView.setText(String.format(Locale.CANADA, "Total items: %d", inventory.getCount()));
            totalValueTextView.setText(String.format(Locale.CANADA, "Total Value: $%.2f", inventory.getInventoryEstimatedValue()));
        } else {
            totalItemsTextView.setText(String.format(Locale.CANADA, "Filtering %d of %d", inventory.getDisplayedItems().size(), inventory.getCount()));
            totalValueTextView.setText(String.format(Locale.CANADA, "Showing: $%.2f", inventory.getDisplayedEstimatedValue()));
        }
        inventoryAdapter.notifyDataSetChanged();
        if (updateDB) {
            FirestoreDB.getInventoriesRef(username).document(inventory.getId()).set(inventory);
        }
    }

    /**
     * Queries for user's items in the selected inventory and add to the current inventory
     */
    public void populateInventoryItems() {
        FirestoreDB.getItemsRef(username, inventory).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Item item = documentSnapshot.toObject(Item.class);
                inventory.addItem(item);
            }
            inventory.sortItems();
            updateTotals(false);
        }).addOnFailureListener(e -> {
            // TODO
        });
    }
}
