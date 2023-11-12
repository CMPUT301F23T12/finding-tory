package com.example.finding_tory;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class ImageAdapter extends ArrayAdapter<Bitmap> {
    private ArrayList<Bitmap> bitmaps;
    private Context context;

    public ImageAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        super(context, R.layout.picture_layout, bitmaps);
        this.bitmaps = bitmaps;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.picture_layout, parent, false);

        // find TextView elements that we wish to update
        Bitmap bitmap = bitmaps.get(position);
        ImageView image = view.findViewById(R.id.image_listview);
        image.setImageBitmap(bitmap);
        TextView imageTitle = view.findViewById(R.id.image_title);
        imageTitle.setText("image-" + Integer.toString(position+1));

        return view;
    }
}
