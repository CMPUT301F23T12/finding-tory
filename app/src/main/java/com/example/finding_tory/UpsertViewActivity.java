package com.example.finding_tory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is responsible for updating/inserting items in an inventory
 */
public class UpsertViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Button add_tags_button;
    private Button submit_button;
    private Button cancel_button;
    private ImageButton scan_barcode_button;
    private TextView view_title;
    private LinearLayout tags_container;
    private EditText description_text;
    private EditText make_text;
    private EditText model_text;
    private EditText date_purchased_text;
    private EditText estimated_cost_text;
    private EditText serial_number_text;
    private EditText comment_text;
    private EditText tags_entered;
    private Item item;
    private boolean isAdd = false;
    private ArrayList<String> tags = new ArrayList<>();
    private String username;
    private Inventory inventory;

    /**
     * Initializes the activity, sets up the user interface, and prepares data models.
     * It determines whether the operation is adding a new item or editing an existing one
     * and sets up the UI accordingly.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsert_view);
        add_tags_button = findViewById(R.id.add_tags_button);
        submit_button = findViewById(R.id.add_button);
        cancel_button = findViewById(R.id.cancel_button);
        scan_barcode_button = findViewById(R.id.scan_barcode_button);
        view_title = findViewById(R.id.upsert_title);
        tags_container = findViewById(R.id.tags_container);
        description_text = findViewById(R.id.description_edittext);
        make_text = findViewById(R.id.make_edittext);
        model_text = findViewById(R.id.model_edittext);
        date_purchased_text = findViewById(R.id.date_edittext);
        estimated_cost_text = findViewById(R.id.amount_edittext);
        estimated_cost_text.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
        serial_number_text = findViewById(R.id.serial_number_edittext);
        comment_text = findViewById(R.id.comment_edittext);
        tags_entered = findViewById(R.id.add_tags_edittext);

        Bundle extras = getIntent().getExtras();
        item = null;
        // if no data is sent through intent, then user wants to add an item
        if (extras != null) {
            item = (Item) (extras.getSerializable("selectedItem"));
            inventory = (Inventory) extras.getSerializable("inventory");
            username = (String) extras.getSerializable("username");
            // if item != null then we are editing an item
            if (item != null) {
                tags.addAll(item.getItemTags());
            } else {
                isAdd = true;
            }
        }
        //initializes UI based on if user wants to add or edit item
        if (isAdd) {
            view_title.setText("Add Item Information");
            submit_button.setText("Add");
            date_purchased_text.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        } else {
            view_title.setText("Edit Item Information");
            description_text.setText(item.getDescription());
            make_text.setText(item.getMake());
            model_text.setText(item.getModel());
            date_purchased_text.setText(new SimpleDateFormat("yyyy-MM-dd").format(item.getPurchaseDate()));
            estimated_cost_text.setText(String.format(Locale.CANADA, "%.2f", item.getEstimatedValue()));
            serial_number_text.setText(item.getSerialNumber());
            comment_text.setText(item.getComment());
            submit_button.setText("Update");
            for (String tag : tags) {
                View tagView = LayoutInflater.from(this).inflate(R.layout.tag_item_layout, tags_container, false);
                TextView tagTextView = tagView.findViewById(R.id.tag_text);
                tagTextView.setText(tag);
                ImageButton removeTagButton = tagView.findViewById(R.id.remove_tag_button);
                removeTagButton.setOnClickListener(v -> {
                    tags_container.removeView(tagView);
                    tags.remove(tag);
                });
                tags_container.addView(tagView);
            }
        }

        /**
         * Display a Date Picker fragment on screen for user to pick a date from a calendar
         */
        date_purchased_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                DatePickerFragment DatePickerDialogFragment;
                DatePickerDialogFragment = new DatePickerFragment();

                if (!date_purchased_text.getText().toString().equals("")) {
                    bundle.putString("date", date_purchased_text.getText().toString());
                }

                DatePickerDialogFragment.setArguments(bundle);
                DatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
            }
        });

        /**
         * User scans a barcode to fill out serial number field
         */
        scan_barcode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();
            }
        });

        /**
         * Displays any tags user entered in the search bar (space-separated) adds it to all
         * tags associated with the item
         */
        add_tags_button.setOnClickListener(view -> {
            String tagText = tags_entered.getText().toString().trim();
            String[] tagParts = tagText.split("\\s+");
            for (String tag : tagParts) {
                if (!tag.isEmpty() && !tags.contains(tag)) {
                    tags.add(tag); // Add the tag to your tags list
                    LayoutInflater inflater = LayoutInflater.from(this);

                    View tagView = inflater.inflate(R.layout.tag_item_layout, tags_container, false);
                    TextView tagTextView = tagView.findViewById(R.id.tag_text);
                    tagTextView.setText(tag);
                    ImageButton removeTagButton = tagView.findViewById(R.id.remove_tag_button);
                    removeTagButton.setOnClickListener(v -> {
                        tags_container.removeView(tagView);
                        tags.remove(tag);
                    });
                    tags_container.addView(tagView);
                }
            }
            tags_entered.setText("");
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
                    error = Item.errorHandleItemInput(date_purchased_text.getText().toString(), description_text.getText().toString(), estimated_cost_text.getText().toString());
                } catch (ParseException e) {
                    error = "Invalid Date Format";
                }

                if (!error.equals("")) {
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
                    intent.putExtra("item_to_add", upsert_item);
                    if (isAdd) {
                        addItemToFirestore(upsert_item);
                    } else {
                        item.setDescription(description);
                        item.setMake(make);
                        item.setModel(model);
                        item.setEstimatedValue(estimated_cost);
                        item.setSerialNumber(serial_number);
                        item.setComment(comment);
                        item.setItemTags(tags);

                        System.out.println(item.getId());
                        FirestoreDB.editItemFromFirestore(username, inventory, item);
                        intent.putExtra("editedItem", item);
                    }
                    setResult(RESULT_OK, intent); // sends item back to parent activity
                    finish();
                }
            }
        });

        /**
         * Will go back to parent activity
         */
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * Adds an item to Firestore items collection.
     *
     * @param item The item to be added to Firestore.
     */
    private void addItemToFirestore(Item item) {
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getItemsRef(username, inventory.getInventoryName()).add(item).addOnSuccessListener(documentReference -> {
                // Get the generated ID and store it in the item
                String generatedId = documentReference.getId();
                item.setId(generatedId); // Assuming Item has a setId method

                // Update the item in Firestore with its ID
                FirestoreDB.getItemsRef(username, inventory.getInventoryName()).document(generatedId).set(item);

                Toast.makeText(UpsertViewActivity.this, "Item added successfully added!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Handle failure
                e.printStackTrace();
            });
        }
    }

    /**
     * Launches activity to scan barcode of a product
     */
    public void scanBarcode() {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barcodeLauncher.launch(options);
    }

    // retrieves data from barcode scanner and displays it to serial number field
    ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            TestBarcodeContainer testBarcode = new TestBarcodeContainer();
            Barcode barcode = testBarcode.getBarcodeInfo(result.getContents());
            if (barcode != null) {
                description_text.setText(barcode.getDescription());
                make_text.setText(barcode.getMake());
                model_text.setText(barcode.getModel());
                estimated_cost_text.setText(barcode.getCost());
            } else {
                Toast.makeText(UpsertViewActivity.this, "Invalid Barcode Scanned.", Toast.LENGTH_SHORT).show();
            }
        }
    });

    /**
     * Gets the date from the dater picker and formats it to be displayed to user
     *
     * @param view       the date picker picker
     * @param year       the year picked
     * @param month      the month picked
     * @param dayOfMonth the day picked
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = sdf.format(calendar.getTime());
        date_purchased_text.setText(selectedDate);
    }
}