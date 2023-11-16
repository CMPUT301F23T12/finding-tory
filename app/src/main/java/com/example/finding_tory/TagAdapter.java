package com.example.finding_tory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * TagAdapter is a custom adapter for a RecyclerView that displays a list of tags.
 * It manages a list of tags and their selected state.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<String> tagList;
    private List<String> selectedTags;

    /**
     * Constructs a TagAdapter with the provided tag list and selected tags.
     *
     * @param tagList      The list of tags to be displayed.
     * @param selectedTags The list of tags that are initially selected.
     */
    public TagAdapter(List<String> tagList, List<String> selectedTags) {
        this.tagList = tagList;
        this.selectedTags = selectedTags;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item_layout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = tagList.get(position);
        holder.tagTextView.setText(tag);
        holder.itemView.setSelected(selectedTags.contains(tag)); // Set the selected state

        holder.itemView.setOnClickListener(v -> {
            if (selectedTags.contains(tag)) {
                selectedTags.remove(tag);
                v.setSelected(false);
            } else {
                selectedTags.add(tag);
                v.setSelected(true);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return tagList.size();
    }

    /**
     * ViewHolder class for the TagAdapter.
     * Holds the TextView used to display the tag's name.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagTextView;

        /**
         * Constructs a ViewHolder for the TagAdapter.
         *
         * @param view The view that will hold the tag text.
         */
        public ViewHolder(View view) {
            super(view);
            tagTextView = view.findViewById(R.id.tag_text);
        }
    }
}
