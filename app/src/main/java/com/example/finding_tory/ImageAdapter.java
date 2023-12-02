package com.example.finding_tory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<String> {
    private ArrayList<String> links;
    private Context context;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public ImageAdapter(Context context, ArrayList<String> links) {
        super(context, R.layout.picture_layout, links);
        this.links = links;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.picture_layout, parent, false);
        }

        // find Imageview and TextView elements that we wish to update
        ImageView image = view.findViewById(R.id.image_view);

        // get the download link, then get the image from the download link and display
        String link = links.get(position);
        Glide.with(context).load(link).into(image);

        TextView imageTitle = view.findViewById(R.id.image_title);
        imageTitle.setText("image-" + (position + 1));

        // make the image delete buttons functional
        ImageButton delete_button = view.findViewById(R.id.remove_image_button);
        delete_button.setOnClickListener(v -> {
            if (onDeleteButtonClickListener != null) {
                onDeleteButtonClickListener.onDeleteButtonClick(position);
            }
        });

        return view;
    }
    /**
     * sets a listener to an adapter in a parent class that will allow the adapter to listen for clicks here
     * called for the image adapter when a new upsertview activity is started
     */
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }
    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }
}
