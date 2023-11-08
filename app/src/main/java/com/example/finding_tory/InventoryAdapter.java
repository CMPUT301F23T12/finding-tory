package com.example.finding_tory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class InventoryAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> items;
    private Context context;

    public InventoryAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
    }

    /**
     * Updates the contents of the TextView elements in an inventory row (ie. an item) to reflect
     *  the state of a given Item object.
     *
     * @param position index of the Item object whose info we wish to update
     * @param convertView the view that will be returned at the end
     * @param parent used to define return in case convertView is null
     *
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

        // modify TextViews with current item information
        descriptionTextView.setText(item.getDescription());
        valueTextView.setText(String.format(Locale.CANADA, "Value : $%.2f", item.getValue()));
        tagsTextView.setText(item.getTags());
        return view;
    }
}
