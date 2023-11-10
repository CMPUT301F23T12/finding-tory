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

/**
 * Implements adapter functionality in order to link the LedgerFragment's ListView
 *  element to the array of Inventories in a Ledger.
 */
public class LedgerAdapter extends ArrayAdapter<Inventory> {

    private ArrayList<Inventory> inventories;
    private Context context;

    /**
     * Creates a new LedgerAdapter object.
     * @param context
     *          view/element this adapter is subordinate to
     * @param inventories
     *          list of Inventory objects being wrapped into the ListView
     */
    public LedgerAdapter(Context context, ArrayList<Inventory> inventories) {
        super(context, 0, inventories);
        this.inventories = inventories;
        this.context = context;
    }


    /**
     * Updates the contents of the TextView elements in a ledger row (ie. an inventory) to reflect
     *  the state of a given Inventory object.
     *
     * @param position
     *          index of the Item object whose info we wish to update
     * @param convertView
     *          view that will be returned at the end
     * @param parent
     *          used to define return in case convertView is null
     *
     * @return the element that is updated
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.inventory_content, parent, false);

        // find TextView elements that we wish to update
        Inventory inventory = inventories.get(position);
        TextView nameTextView = view.findViewById(R.id.name_text);
        TextView countTextView = view.findViewById(R.id.count_text);
        TextView valueTextView = view.findViewById(R.id.value_text);

        // modify TextViews with current item information
        nameTextView.setText(inventory.getName());
        countTextView.setText(String.format(Locale.CANADA, "Total items: %d",  inventory.getCount()));
        valueTextView.setText(String.format(Locale.CANADA, "Total Value : $%.2f", inventory.getValue()));
        return view;
    }
}
