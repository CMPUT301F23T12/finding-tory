package com.example.finding_tory;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * ItemViewActivity is an AppCompatActivity that displays detailed information about a selected item.
 * It allows users to view, edit, or delete an item, and navigate back to the inventory list.
 *
 * This activity handles various user interactions such as editing item details, confirming item deletion,
 * and updating item views.
 */
public class ItemViewActivity extends AppCompatActivity {
    Item selectedItem;
    int position;

    /**
     * Initializes the activity, sets up the user interface, and retrieves the selected item from the intent.
     * Populates the UI components with the item's details.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        setTitle("View Item");

        // Retrieve the selected item from the Intent
        selectedItem = (Item) getIntent().getSerializableExtra("selectedItem");
        position = getIntent().getIntExtra("pos", 0);

        // Use the selectedItem to populate the views in the item_click_view layout
        assert selectedItem != null;
        setItemView(selectedItem);

        // Handles going back to Inventory view
        final FloatingActionButton closeItemViewButton = findViewById(R.id.button_close_item_view);
        closeItemViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("returnedItem", selectedItem);
                intent.putExtra("position", position);
                intent.putExtra("action", "edit");

                setResult(RESULT_OK, intent);
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
                        data.putExtra("pos", getIntent().getIntExtra("pos", -1));
                        data.putExtra("action", "delete");
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
                Intent editItemIntent = new Intent(ItemViewActivity.this, UpsertViewActivity.class);
                editItemIntent.putExtra("selectedItem", selectedItem);
                startActivityForResult(editItemIntent, ActivityCodes.EDIT_ITEM.getRequestCode());
            }
        });

        final Button leftPictureButton = findViewById(R.id.button_left_picture_item);
        leftPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Will Implement Later
            }
        });

        final Button rightPictureButton = findViewById(R.id.button_right_picture_item);
        rightPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Will Implement Later
            }
        });
    }

    /**
     * Handles the result from the edit item activity.
     * Updates the item view if the item was edited.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCodes.EDIT_ITEM.getRequestCode()) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Item returnedItem = (Item) data.getSerializableExtra("editedItem");
                assert returnedItem != null;
                selectedItem = returnedItem;
                setItemView(selectedItem);
            }
        }
    }

    /**
     * Populates the UI components with the item's details.
     *
     * @param passedItem The item whose details are to be displayed.
     */
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
        item_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(passedItem.getPurchaseDate()));

        TextView item_value = findViewById(R.id.item_value_text);
        item_value.setText(String.format(Locale.CANADA, "$%.2f", passedItem.getEstimatedValue()));

        TextView item_serial_number = findViewById(R.id.item_serial_number_text);
        item_serial_number.setText(passedItem.getSerialNumber());

        TextView item_tags = findViewById(R.id.item_tags);
        item_tags.setText(passedItem.getTagsString());
    }
}
