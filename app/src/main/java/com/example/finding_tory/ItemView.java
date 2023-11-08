package com.example.finding_tory;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_click_view);

        // Retrieve the selected item from the Intent
        Item selectedItem = (Item) getIntent().getSerializableExtra("item");

        // Use the selectedItem to populate the views in the item_click_view layout
        TextView item_name = findViewById(R.id.item_name_text);
        item_name.setText(selectedItem.getName());
        TextView item_comment = findViewById(R.id.item_comment_text);
        item_comment.setText(selectedItem.getComment());
        TextView item_make = findViewById(R.id.item_make_text);
        item_make.setText(selectedItem.getMake());
        TextView item_date = findViewById(R.id.item_date_text);
        item_date.setText(selectedItem.getDate().toString());
        TextView item_value = findViewById(R.id.item_value_text);
        item_value.setText(selectedItem.getValue().toString());
        TextView item_quantity = findViewById(R.id.item_quantity_text);
        item_quantity.setText(selectedItem.getMake().toString());
        TextView item_serial_number = findViewById(R.id.item_serial_number_text);
        item_serial_number.setText(selectedItem.getSerialNumber());


        // Handles going back to Inventory view
        final Button closeItemViewButton = findViewById(R.id.button_close_item_view);
        closeItemViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Return to Inventory View
            }
        });

        final Button deleteItemButton = findViewById(R.id.button_delete_item);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Show Delete Confirmation Fragment
                DeleteItemFragment().show(getSupportFragmentManager(), "DELETE_ITEM");
            }
        });

        final Button editItemButton = findViewById(R.id.button_edit_item);
        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Start Edit Activity
                Intent editItemIntent = new Intent(ItemActivity.this, EditItemActivity.class);
                editItemIntent.putExtra("selectedItem", selectedItem);
                startActivity(editItemIntent);
            }
        });

        final Button leftPictureButton = findViewById(R.id.button_left_picture_item);
        leftPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Will Implement Later
            }
        });

        final Button rightPictureButton = findViewById(R.id.button_right_picture_item);
        rightPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // Will Implement Later
            }
        });
    }
}
