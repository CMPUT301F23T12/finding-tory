package com.example.finding_tory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;


/**
 * Displays the content of a given Inventory (ie. a list of Items).
 */
public class InventoryViewActivity extends AppCompatActivity {

    private Inventory inventory;
    private ListView inventoryListView;
    private ArrayAdapter<Item> inventoryAdapter;

    private TextView totalItemsTextView;
    private TextView totalValueTextView;
    private FloatingActionButton addItemButton;

    /**
     * Initializes the instance variables and bindings associated with this activity on creation.
     *
     * @param savedInstanceState possible default layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_view);

        // TODO get an actual inventory from the db
        // get the inventory that's been passed by the ledger view parent activity
        Intent intent = getIntent();
        inventory = (Inventory) intent.getSerializableExtra("inventory");
        assert (inventory != null);
        setTitle(inventory.getName());

        // map the listview to the inventory's list of items via custom inventory adapter
        inventoryListView = findViewById(R.id.inventory_listview);
        inventoryAdapter = new InventoryAdapter(this, inventory.getItems());
        inventoryListView.setAdapter(inventoryAdapter);

        // initialize and cache the TextViews for the totals
        totalItemsTextView = findViewById(R.id.total_items_textview);
        totalValueTextView = findViewById(R.id.total_value_textview);
        addItemButton = findViewById(R.id.add_item_button);
        updateTotals();

        // allows new items to be added
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add start activity page for result for adding item
            }
        });

        //allow the items in the list to be clickable
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Item selectedItem = inventoryAdapter.getItem(position);

                Intent intent = new Intent(InventoryViewActivity.this, ItemViewActivity.class);
                intent.putExtra("item", selectedItem);

                startActivity(intent);
            }
        });
    }


    /**
     * Rewrites the TextView elements displaying the inventory totals to reflect new values.
     */
    public void updateTotals() {
        totalItemsTextView.setText(
                String.format(Locale.CANADA,
                        "Total items: %d",
                        inventory.getCount())
        );
        totalValueTextView.setText(
                String.format(Locale.CANADA,
                        "Total Value: $%.2f",
                        inventory.getValue())
        );
    }
}
