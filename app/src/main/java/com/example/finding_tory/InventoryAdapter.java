package com.example.finding_tory;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
        TextView tagsTextView = view.findViewById(R.id.tags_text);
        CheckBox checkBox = view.findViewById(R.id.item_checkbox);
        checkBox.setChecked(selectedItems.get(position, false));

        // modify TextViews with current item information
        descriptionTextView.setText(item.getDescription());
        valueTextView.setText(String.format(Locale.CANADA, "Value : $%.2f", item.getEstimatedValue()));

        StringBuilder tag_to_display = new StringBuilder();
        for (String tag : item.getItemTags()) {
            tag_to_display.append(tag).append(" ");
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(position);
            }
        });
        tagsTextView.setText(item.getTagsString());

        return view;
    }

    private void toggleSelection(int position) {
        boolean isSelected = selectedItems.get(position, false);
        selectedItems.put(position, !isSelected);
        notifyDataSetChanged();
    }

    // Returns the list of selected items
    public List<Item> getSelectedItems() {
        List<Item> selected = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (selectedItems.get(i, false)) {
                selected.add(items.get(i));
            }
        }
        return selected;
    }

    // Method to clear the selection of all items
    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
}
