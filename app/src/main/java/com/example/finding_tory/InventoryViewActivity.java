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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        ArrayList<String> tags1 = new ArrayList<String>();
        tags1.add("testtag1");
        tags1.add("testtag1.1");
        ArrayList<String> tags2 = new ArrayList<String>();
        tags2.add("testtag2");
        tags2.add("testtag2.2");
        inventory.addItem(new Item(new Date(2023, 1, 24), "Item1", "make1", "model1", 10.01f, 1, "no comment",tags1));
        inventory.addItem(new Item(new Date(2023, 1, 25), "Item2", "make2", "model2", 20.02f, 2, "No comment",tags2));
        inventory.addItem(new Item(new Date(2023, 2, 27), "Item3", "make1", "model3", 30.03f, 3, "no Comment",tags2));
        inventory.addItem(new Item(new Date(2022, 9, 13), "Item4", "make4", "model1", 400.40f, 4, "no commenT",tags1));

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
