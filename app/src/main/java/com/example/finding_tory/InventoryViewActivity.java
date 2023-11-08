package com.example.finding_tory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;


/**
 * Displays the content of a given Inventory (ie. a list of Items).
 */
public class InventoryViewActivity extends AppCompatActivity {

    private Inventory inventory;
    private ListView inventoryListView;
    private ArrayAdapter<MockItem> inventoryAdapter;

    private TextView totalItemsTextView;
    private TextView totalValueTextView;
    private Button addItemButton;

    /**
     * Initializes the instance variables and bindings associated with this activity on creation.
     *
     * @param savedInstanceState
     *          possible default layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_view);

        // TODO get an actual inventory from the db
        inventory = new Inventory("Dummy Inventory");
        setTitle(inventory.getName());

        // create some mock items to populate the list
        inventory.addItem(new MockItem("Item1", 1.1, "[test]"));
        inventory.addItem(new MockItem("Item2", 2.2, "[test] [uniqueTag]"));
        inventory.addItem(new MockItem("Item3", 3.3, "[test]"));
        inventory.addItem(new MockItem("Item4", 4.4, "[test]"));

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: let the user view the item (start new activity)
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
