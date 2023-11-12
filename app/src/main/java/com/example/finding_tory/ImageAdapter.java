package com.example.finding_tory;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Uri> {
    private ArrayList<Uri> uris;
    private Context context;

    public ImageAdapter(Context context, ArrayList<Uri> uris) {
        super(context, R.layout.picture_layout, uris);
        this.uris = uris;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.picture_layout, parent, false);

        // find Imageview and TextView elements that we wish to update
        Uri uri = uris.get(position);
        ImageView image = view.findViewById(R.id.image_view);
        image.setImageURI(uri);
        TextView imageTitle = view.findViewById(R.id.image_title);
        imageTitle.setText("image-" + Integer.toString(position+1));

        return view;
    }
}
