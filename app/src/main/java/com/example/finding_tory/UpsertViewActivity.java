package com.example.finding_tory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * This class is responsible for updating/inserting items in an inventory
 */
public class UpsertViewActivity extends AppCompatActivity{
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
    Item item;
    boolean isAdd = false;
    ArrayList<String> tags = new ArrayList<>();

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
        item = null;
        // if no data is sent through intent, then user wants to add an item
        if (extras == null)
            isAdd = true;
        else {
            item = (Item) (extras.getSerializable("selectedItem"));
            tags.addAll(item.getItemTags());
        }

        //initializes UI based on if user wants to add or edit item
        if (isAdd) {
            view_title.setText("Add Item Information");
            submit_button.setText("Add");
        } else {
            view_title.setText("Edit Item Information");
            description_text.setText(item.getDescription());
            make_text.setText(item.getMake());
            model_text.setText(item.getModel());
            date_purchased_text.setText(new SimpleDateFormat("yyyy-MM-dd").format(item.getPurchaseDate()));
            estimated_cost_text.setText(Float.toString(item.getEstimatedValue()));
            serial_number_text.setText(item.getSerialNumber());
            comment_text.setText(item.getComment());
            tags_view.setText(tags.toString());
            submit_button.setText("Update");
        }

        /**
         * Displays any tags user entered in the search bar (space-separated) adds it to all
         * tags associated with the item
         */
        add_tags_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tags_string = tags_entered.getText().toString();
                if (tags_string.equals("")) {
                    return;
                }
                String[] tag_list = tags_string.split(" ");
                tags.addAll(Arrays.asList(tag_list)); // adds to all tags for item
                tags_view.setText(tags.toString());
                tags_entered.setText("");
            }
        });

        /**
         * Submits the form if data is valid and will either add item to inventory list or
         * view item if user wants to edit item
         */
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checks for error and makes sure required input is filled
                String error;
                try {
                    error = Item.errorHandleItemInput(
                            date_purchased_text.getText().toString(),
                            description_text.getText().toString(),
                            estimated_cost_text.getText().toString());
                } catch (ParseException e) {
                    error = "Invalid Date Format";
                }

                if (!error.equals("")){
                    // TODO: display error message on screen
                    Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Intent intent = new Intent();
                    // takes input from the form
                    Date dateFormatted = null;
                    try {
                        dateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(date_purchased_text.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String description = description_text.getText().toString();
                    String make = make_text.getText().toString();
                    String model = model_text.getText().toString();
                    float estimated_cost = Float.parseFloat(estimated_cost_text.getText().toString());
                    String serial_number = serial_number_text.getText().toString();
                    String comment = comment_text.getText().toString();
                    Item upsert_item = new Item(dateFormatted, description, make, model, estimated_cost, serial_number, comment, tags);

                    if (isAdd) {
                        intent.putExtra("item_to_add", upsert_item);
                    } else {
                        intent.putExtra("editedItem", upsert_item);
                    }
                    setResult(RESULT_OK, intent); // sends item back to parent activity
                    finish();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}