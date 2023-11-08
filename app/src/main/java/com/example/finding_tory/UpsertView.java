package com.example.finding_tory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class UpsertView extends AppCompatActivity {
    Button add_tags_button;
    Button submit_button;
    Button cancel_button;
    TextView view_title;
    TextView tags_view;
    EditText description_text;
    EditText make_text;
    EditText model_text;
    EditText date_purchased_text;
    EditText cost_text;
    EditText serial_number_text;
    EditText comment_text;
    EditText tags_entered;
    ArrayList<String> tags;

    boolean isAdd = true;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsert_view);
        add_tags_button = findViewById(R.id.add_tags_button);
        submit_button = findViewById(R.id.add_button);
        cancel_button = findViewById(R.id.cancel_button);
        view_title = findViewById(R.id.upsert_title);
        tags_view = findViewById(R.id.tag_display);
        description_text = findViewById(R.id.description_edittext);
        make_text = findViewById(R.id.make_edittext);
        model_text = findViewById(R.id.model_edittext);
        date_purchased_text = findViewById(R.id.date_edittext);
        cost_text = findViewById(R.id.amount_edittext);
        cost_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10,2)});
        serial_number_text = findViewById(R.id.serial_number_edittext);
        comment_text = findViewById(R.id.comment_edittext);
        tags_entered = findViewById(R.id.add_tags_edittext);


        Item item;
        Date purchaseDate = new Date();
        String description = "Test Item";
        String make = "Test Make";
        String model = "Test Model";
        int serialNumber = 12345;
        Float estimatedValue = 100.0f;
        String comment = "Test Comment";
        ArrayList<String> tags_test =new ArrayList<String>();
        tags_test.add("tag1");
        tags_test.add("tag2");
        item = new Item(purchaseDate, description, make, model, estimatedValue, serialNumber, comment, tags_test);

        if (!isAdd) {
            view_title.setText("Edit Item Information");
            description_text.setText(item.getDescription());
            make_text.setText(item.getMake());
            model_text.setText(item.getModel());
            date_purchased_text.setText((CharSequence) item.getPurchaseDate());
            cost_text.setText(Float.toString(item.getEstimatedValue()));
            serial_number_text.setText(Integer.toString(item.getSerialNumber()));
            comment_text.setText(item.getComment());
        }

        add_tags_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tags_string = tags_entered.getText().toString();
                String[] tag_list = tags_string.split(" ");
                tags = new ArrayList<>(Arrays.asList(tag_list));
                for (String tag: tags) {
                    String previous = tags_view.getText().toString();
                    tags_view.setText(previous + " (" + tag + ")");
                }
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dateFormatted = null;
                try {
                    dateFormatted = new SimpleDateFormat("yyyy-MM").parse(date_purchased_text.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String description = description_text.getText().toString();
                String make = make_text.getText().toString();
                String model = model_text.getText().toString();
                Float cost = Float.parseFloat(cost_text.getText().toString());
                int serial_number = Integer.parseInt(serial_number_text.getText().toString());
                String comment = comment_text.getText().toString();
//                Item item = new Item(dateFormatted, description, make, model, cost, serial_number, comment, tags)
                //TODO: send item back to be displayed in list/update
//                send as intents
//                finish()
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}