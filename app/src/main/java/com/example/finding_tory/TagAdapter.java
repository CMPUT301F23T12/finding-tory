package com.example.finding_tory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<String> tagList;
    private List<String> selectedTags;

    public TagAdapter(List<String> tagList, List<String> selectedTags) {
        this.tagList = tagList;
        this.selectedTags = selectedTags;
    }

    @NonNull
    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tag = tagList.get(position);
        holder.tagTextView.setText(tag);
        holder.itemView.setSelected(selectedTags.contains(tag)); // Set the selected state

        holder.itemView.setOnClickListener(v -> {
            if(selectedTags.contains(tag)) {
                selectedTags.remove(tag);
                v.setSelected(false);
            } else {
                selectedTags.add(tag);
                v.setSelected(true);
            }
            // Notify any other component if necessary, or you can just update the UI
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tagTextView;

        public ViewHolder(View view) {
            super(view);
            tagTextView = view.findViewById(R.id.tagTextView); // Assuming you have a TextView with this ID in your tag_item.xml
        }
    }
}
