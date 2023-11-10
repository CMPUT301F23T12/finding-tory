package com.example.finding_tory;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public class ItemViewActivity extends AppCompatActivity implements Serializable {
    private static final int EDIT_ITEM_REQUEST = 1; // Can be any integer unique to this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        setTitle("View Item");

        // Retrieve the selected item from the Intent
        Item selectedItem = (Item) getIntent().getSerializableExtra("item");

        // Use the selectedItem to populate the views in the item_click_view layout
        assert selectedItem != null;
        setItemView(selectedItem);

        // Handles going back to Inventory view
        final FloatingActionButton closeItemViewButton = findViewById(R.id.button_close_item_view);
        closeItemViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Return to Inventory View
            }
        });

        final Button deleteItemButton = findViewById(R.id.button_delete_item);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show Delete Confirmation Fragment
                DeleteItemFragment deleteDialog = new DeleteItemFragment();
                final View greyBack = findViewById(R.id.fadeBackground);
                greyBack.setVisibility(View.VISIBLE);
                // Set the listener to know when the dialog is dismissed
                deleteDialog.setDeleteDialogListener(new DeleteItemFragment.DeleteDialogListener() {
                    @Override
                    public void onDialogDismissed() {
                        // Make grey background invisible when the dialog is dismissed
                        greyBack.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDeleteConfirmed() {
                        Intent data = new Intent();
                        data.putExtra("position", getIntent().getIntExtra("position", -1));
                        // You can add extra data if needed
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
                deleteDialog.show(getSupportFragmentManager(), "DELETE_ITEM");
            }
        });

        final Button editItemButton = findViewById(R.id.button_edit_item);
        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Edit Activity
//                Intent editItemIntent = new Intent(ItemView.this, EditItemActivity.class);
//                editItemIntent.putExtra("selectedItem", selectedItem);
//                startActivityForResult(editItemIntent, EDIT_ITEM_REQUEST);
            }
        });

        final Button leftPictureButton = findViewById(R.id.button_left_picture_item);
        leftPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Will Implement Later
            }
        });

        final Button rightPictureButton = findViewById(R.id.button_right_picture_item);
        rightPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Will Implement Later
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Item selectedItem = (Item) data.getSerializableExtra("editedItem");
                assert selectedItem != null;
                selectedItem.updateItem((Item) Objects.requireNonNull(data.getSerializableExtra("editedItem")));
                setItemView(selectedItem);
            }
        }
    }

    private void setItemView(Item passedItem) {
        TextView item_name = findViewById(R.id.item_description_text);
        item_name.setText(passedItem.getDescription());

        TextView item_comment = findViewById(R.id.item_comment_text);
        item_comment.setText(passedItem.getComment());

        TextView item_make = findViewById(R.id.item_make_text);
        item_make.setText(passedItem.getMake());

        TextView item_quantity = findViewById(R.id.item_model_text);
        item_quantity.setText(passedItem.getModel());

        TextView item_date = findViewById(R.id.item_date_text);
        item_date.setText(passedItem.getPurchaseDate().toString());

        TextView item_value = findViewById(R.id.item_value_text);
        item_value.setText(String.format(Locale.CANADA, "$%.2f", passedItem.getEstimatedValue()));

        TextView item_serial_number = findViewById(R.id.item_serial_number_text);
        item_serial_number.setText(passedItem.getSerialNumber());
    }
}
