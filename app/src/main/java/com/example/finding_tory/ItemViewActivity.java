package com.example.finding_tory;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * ItemViewActivity is an AppCompatActivity that displays detailed information about a selected item.
 * It allows users to view, edit, or delete an item, and navigate back to the inventory list.
 * <p>
 * This activity handles various user interactions such as editing item details, confirming item deletion,
 * and updating item views.
 */
public class ItemViewActivity extends AppCompatActivity {
    private String username;
    private Inventory selectedInventory;
    private Item selectedItem;
    int picture_index;
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
        username = (String) getIntent().getSerializableExtra("username");
        selectedInventory = (Inventory) getIntent().getSerializableExtra("inventory");
        selectedItem = (Item) getIntent().getSerializableExtra("selectedItem");
        position = getIntent().getIntExtra("pos", 0);

        // Use the selectedItem to populate the views in the item_click_view layout
        assert selectedItem != null;
        setItemView(selectedItem);

        // Handles going back to Inventory view (whether by phone back button or top-right X button)
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            // we need to override standard back behaviour because inventory view expects a result code
            @Override
            public void handleOnBackPressed() {
                handleOnBack();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        final FloatingActionButton closeItemViewButton = findViewById(R.id.button_close_item_view);
        closeItemViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOnBack();
            }
        });

        // handles delete button being pressed (provides confirmation dialog: return to inventory if yes)
        final Button deleteItemButton = findViewById(R.id.button_delete_item);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show Delete Confirmation Fragment
                DeleteConfirmationFragment deleteDialog = new DeleteConfirmationFragment();
                final View greyBack = findViewById(R.id.fadeBackground);
                greyBack.setVisibility(View.VISIBLE);
                // Set the listener to know when the dialog is dismissed
                deleteDialog.setDeleteDialogListener(new DeleteConfirmationFragment.DeleteDialogListener() {
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

        // set click handler for edit button (launches an edit item activity)
        final Button editItemButton = findViewById(R.id.button_edit_item);
        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start Edit Activity
                Intent editItemIntent = new Intent(ItemViewActivity.this, UpsertViewActivity.class);
                editItemIntent.putExtra("username", username);
                editItemIntent.putExtra("selectedItem", selectedItem);
                editItemIntent.putExtra("inventory", selectedInventory);
                startActivityForResult(editItemIntent, ActivityCodes.EDIT_ITEM.getRequestCode());
            }
        });

        final Button leftPictureButton = findViewById(R.id.button_left_picture_item);
        leftPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // update the displayed image to the previous image in the list
                updateImage(-1, selectedItem);
            }
        });

        final Button rightPictureButton = findViewById(R.id.button_right_picture_item);
        rightPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // update the displayed image to the next image in the list
                updateImage(1, selectedItem);
            }
        });
    }

    /**
     * Handles what to do on a regular "back" action (whether by phone back gesture or X button).
     * Returns to the inventory view, passing the currently viewed item and position back.
     */
    private void handleOnBack() {
        Intent intent = getIntent();
        intent.putExtra("returnedItem", selectedItem);
        intent.putExtra("position", position);
        intent.putExtra("action", "edit");

        setResult(RESULT_OK, intent);
        finish(); // Return to Inventory View
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

        if (passedItem.getComment().equals("")) {
            findViewById(R.id.comment_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.comment_layout).setVisibility(View.VISIBLE);
            TextView item_comment = findViewById(R.id.item_comment_text);
            item_comment.setText(passedItem.getComment());
        }

        if (passedItem.getMake().equals("")) {
            findViewById(R.id.make_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.make_layout).setVisibility(View.VISIBLE);
            TextView item_make = findViewById(R.id.item_make_text);
            item_make.setText(passedItem.getMake());
        }

        if (passedItem.getModel().equals("")) {
            findViewById(R.id.model_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.model_layout).setVisibility(View.VISIBLE);
            TextView item_model = findViewById(R.id.item_model_text);
            item_model.setText(passedItem.getModel());
        }

        if (passedItem.getSerialNumber().equals("")) {
            findViewById(R.id.serial_number_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.serial_number_layout).setVisibility(View.VISIBLE);
            TextView item_serial_number = findViewById(R.id.item_serial_number_text);
            item_serial_number.setText(passedItem.getSerialNumber());
        }

        TextView item_date = findViewById(R.id.item_date_text);
        item_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(passedItem.getPurchaseDate()));

        TextView item_value = findViewById(R.id.item_value_text);
        item_value.setText(String.format(Locale.CANADA, "$%.2f", passedItem.getEstimatedValue()));



        //if the item does not have an empty list of images, set the image view of the item to the first picture of the item
        if (passedItem.getImageLinks() != null && passedItem.getImageLinks().size() > 0) {
            findViewById(R.id.image_slider_placeholder).setVisibility(View.VISIBLE);
            ImageView image = findViewById(R.id.item_image);
            picture_index = 0;
            Glide.with(this).load(passedItem.getImageLinks().get(picture_index)).into(image);
            if (passedItem.getImageLinks().size() == 1) { // if there only one image, no need for switch buttons
                findViewById(R.id.button_right_picture_item).setVisibility(View.GONE);
                findViewById(R.id.button_left_picture_item).setVisibility(View.GONE);
            } else {
                findViewById(R.id.button_right_picture_item).setVisibility(View.VISIBLE);
                findViewById(R.id.button_left_picture_item).setVisibility(View.VISIBLE);
            }
        } else { // if there's no images, no need to show the image section
            findViewById(R.id.image_slider_placeholder).setVisibility(View.GONE);
        }

        if (passedItem.getItemTags().size() == 0) {
            findViewById(R.id.tags_layout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.tags_layout).setVisibility(View.VISIBLE);
            LinearLayout tagsContainer = findViewById(R.id.item_tags_container);
            tagsContainer.removeAllViews();


            for (String tag : passedItem.getItemTags()) {

                LayoutInflater inflater = LayoutInflater.from(this);
                View tagView = inflater.inflate(R.layout.tag_item_layout, tagsContainer, false);
                ImageButton removeTagButton = tagView.findViewById(R.id.remove_tag_button);
                TextView tagTextView = tagView.findViewById(R.id.tag_text);
                tagTextView.setText(tag);
                removeTagButton.setVisibility(View.GONE);
                tagsContainer.addView(tagView);

            }
        }

    }

    /**
     * Updates the image displayed for the item, called when the left or right buttons for image are pressed
     *
     * @param direction (int): either -1 to signify left or 1 to signify right
     *                  item (Item): the item that we are currently viewing
     */
    private void updateImage(int direction, Item item) {
        if (item.getImageLinks() == null || item.getImageLinks().size() == 0) { // return if no images exist
            Toast.makeText(this, "No images to show", Toast.LENGTH_SHORT).show();
            return;
        }
        // load the image and index of the the current image, calculate the next index of image and display
        ImageView image = findViewById(R.id.item_image);
        picture_index += direction + item.getImageLinks().size(); // ensures that picture_index is always positive; mod will account for it
        picture_index %= item.getImageLinks().size();
        Glide.with(this).load(item.getImageLinks().get(picture_index)).into(image);
    }
}
