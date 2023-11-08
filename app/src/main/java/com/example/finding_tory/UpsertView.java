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
public class UpsertView extends AppCompatActivity{
//    private final int ADD_ITEM = 0;
//    private final int EDIT_ITEM = 1;
//    private final int CANCEL_EDIT = 2;

    Button add_tags_button;
    Button submit_button;
    Button cancel_button;
    TextView view_title;
    TextView tags_view;
    EditText description_text;
    EditText make_text;
    EditText model_text;
    EditText date_purchased_text;
    EditText estimated_cost_text;
    EditText serial_number_text;
    EditText comment_text;
    EditText tags_entered;
    ArrayList<String> tags;
    boolean isAdd = false;

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
        estimated_cost_text = findViewById(R.id.amount_edittext);
        estimated_cost_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10,2)});
        serial_number_text = findViewById(R.id.serial_number_edittext);
        comment_text = findViewById(R.id.comment_edittext);
        tags_entered = findViewById(R.id.add_tags_edittext);

        Bundle extras = getIntent().getExtras();
        Item item = null;
        // if no data is sent through intent, then user wants to add an item
        if (extras == null)
            isAdd = true;
        else {
            item = (Item) (extras.getSerializable("ItemToEdit"));
        }

        // sets UI for whether user wants to add/edit
        if (isAdd) {
            view_title.setText("Add Item Information");
            submit_button.setText("Add");
        } else {
            view_title.setText("Edit Item Information");
            description_text.setText(item.getDescription());
            make_text.setText(item.getMake());
            model_text.setText(item.getModel());
            date_purchased_text.setText((CharSequence) item.getPurchaseDate());
            estimated_cost_text.setText(Float.toString(item.getEstimatedValue()));
            serial_number_text.setText(Integer.toString(item.getSerialNumber()));
            comment_text.setText(item.getComment());
            submit_button.setText("Update");
        }

        add_tags_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tags_string = tags_entered.getText().toString();
                String[] tag_list = tags_string.split(" ");
                tags = new ArrayList<>(Arrays.asList(tag_list));
                for (String tag: tags) {
                    String previous = tags_view.getText().toString();
                    tags_view.setText(String.format("%s (%s)", previous, tag));
                }
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String error = "";
                try {
                    error = Item.errorHandleItemInput(
                            date_purchased_text.getText().toString(),
                            description_text.getText().toString(),
                            estimated_cost_text.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (!error.equals("")){
                    // TODO: display error message on screen
                } else {
                    Intent intent = new Intent();
                    Date dateFormatted = null;
                    try {
                        dateFormatted = new SimpleDateFormat("yyyy-MM").parse(date_purchased_text.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String description = description_text.getText().toString();
                    String make = make_text.getText().toString();
                    String model = model_text.getText().toString();
                    Float estimated_cost = Float.parseFloat(estimated_cost_text.getText().toString());
                    int serial_number = Integer.parseInt(serial_number_text.getText().toString());
                    String comment = comment_text.getText().toString();
//                Item item = new Item(dateFormatted, description, make, model, cost, serial_number, comment, tags)
                    //TODO: send item back to be displayed in list/update
                    if (isAdd) {
                        Item new_item = new Item(dateFormatted, description, make, model, estimated_cost, serial_number, comment, tags);
                        intent.putExtra("item_to_do", new_item);
                        setResult(ActivityCodes.ADD_ITEM.getRequestCode(), intent);
                        finish();
                    } else {
                        intent.putExtra("date_edit", dateFormatted);
                        intent.putExtra("description_edit", description);
                        intent.putExtra("make_edit", make);
                        intent.putExtra("model_edit", model);
                        intent.putExtra("estimated_cost_edit", estimated_cost);
                        intent.putExtra("serial_number_edit", serial_number);
                        intent.putExtra("comment_edit", comment);
                        intent.putExtra("tags_edit", tags);
                        setResult(ActivityCodes.EDIT_ITEM.getRequestCode(), intent);
                        finish();
                    }
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(ActivityCodes.CANCEL_ITEM.getRequestCode(), intent);
                finish();
            }
        });
    }
}