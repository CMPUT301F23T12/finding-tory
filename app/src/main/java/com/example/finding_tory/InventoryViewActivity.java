package com.example.finding_tory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class InventoryViewActivity extends AppCompatActivity {

    private Inventory inventory;
    private ListView inventoryListView;
    private ArrayAdapter<Item> inventoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_view);

        // TODO get an actual inventory from the db
        inventory = new Inventory("Dummy Inventory");
        setTitle(inventory.getName());

        // create some mock items to populate the list
        inventory.addItem(new Item("Item1", 1.1, "[test]"));
        inventory.addItem(new Item("Item2", 2.2, "[test] [uniqueTag]"));
        inventory.addItem(new Item("Item3", 3.3, "[test]"));
        inventory.addItem(new Item("Item4", 4.4, "[test]"));

        // map the listview to the inventory's list of items via custom inventory adapter
        inventoryListView = findViewById(R.id.inventory_listview);
        inventoryAdapter = new InventoryAdapter(this, inventory.getItems());
        inventoryListView.setAdapter(inventoryAdapter);
    }
}