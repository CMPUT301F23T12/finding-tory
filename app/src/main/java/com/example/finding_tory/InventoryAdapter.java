package com.example.finding_tory;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implements adapter functionality in order to link the InventoryViewActivity's ListView
 * element to the array of Items in an Inventory.
 */
public class InventoryAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> items;
    private Context context;
    private SparseBooleanArray selectedItems;

    /**
     * Creates a new InventoryAdapter object.
     *
     * @param context view/element this adapter is subordinate to
     * @param items   list of Item objects being wrapped into the ListView
     */
    public InventoryAdapter(Context context, ArrayList<Item> items) {
        super(context, R.layout.item_content, items);
        this.items = items;
        this.context = context;
        this.selectedItems = new SparseBooleanArray();
    }


    /**
     * Updates the contents of the TextView elements in an inventory row (ie. an item) to reflect
     * the state of a given Item object.
     *
     * @param position    index of the Item object whose info we wish to update
     * @param convertView view that will be returned at the end
     * @param parent      used to define return in case convertView is null
     * @return the element that is updated
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);

        // find TextView elements that we wish to update
        Item item = items.get(position);
        TextView descriptionTextView = view.findViewById(R.id.description_text);
        TextView valueTextView = view.findViewById(R.id.value_text);
        CheckBox checkBox = view.findViewById(R.id.item_checkbox);
        checkBox.setChecked(selectedItems.get(position, false));

        // modify TextViews with current item information
        descriptionTextView.setText(item.getDescription());
        valueTextView.setText(String.format(Locale.CANADA, "Value : $%.2f", item.getEstimatedValue()));

        LinearLayout tagsContainer = view.findViewById(R.id.item_tags_container);
        tagsContainer.removeAllViews();

        int maxContainerWidth = 420;  // Set your desired maximum width for the tag container
        int currentContainerWidth = 0;
        boolean limit_approached = false;

        for (String tag : item.getItemTags()) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View tagView = inflater.inflate(R.layout.tag_item_layout, tagsContainer, false);

            ImageButton removeTagButton = tagView.findViewById(R.id.remove_tag_button);
            TextView tagTextView = tagView.findViewById(R.id.tag_text);
            tagTextView.setText(tag);
            tagTextView.setTextSize(11);
            removeTagButton.setVisibility(View.GONE);

            // Measure the width of the tag
            tagView.measure(0, 0);
            int tagWidth = tagView.getMeasuredWidth();

            // Check if adding the tag will exceed the maximum width
            if (currentContainerWidth + tagWidth <= maxContainerWidth) {
                tagsContainer.addView(tagView);
                currentContainerWidth += tagWidth;
            } else {
                // Stop adding more tags if it exceeds the maximum width
                limit_approached = true;
                continue;
            }
        }
        if (limit_approached) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View tagView = inflater.inflate(R.layout.tag_item_layout, tagsContainer, false);

            ImageButton removeTagButton = tagView.findViewById(R.id.remove_tag_button);
            TextView tagTextView = tagView.findViewById(R.id.tag_text);
            tagTextView.setTextSize(11);
            removeTagButton.setVisibility(View.GONE);
            tagTextView.setText("...");
            tagsContainer.addView(tagView);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(position);
            }
        });

        return view;
    }

    /**
     * Toggles the selection status of an item at a given position.
     *
     * @param position The position of the item in the ListView.
     */
    private void toggleSelection(int position) {
        boolean isSelected = selectedItems.get(position, false);
        selectedItems.put(position, !isSelected);
        notifyDataSetChanged();
    }

    /**
     * Returns a list of selected items.
     *
     * @return A list containing the selected items.
     */
    public List<Item> getSelectedItems() {
        List<Item> selected = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (selectedItems.get(i, false)) {
                selected.add(items.get(i));
            }
        }
        return selected;
    }

    /**
     * Clears the selection of all items in the ListView.
     */
    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
}
