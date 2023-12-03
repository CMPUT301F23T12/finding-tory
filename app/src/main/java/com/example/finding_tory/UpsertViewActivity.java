package com.example.finding_tory;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/**
 * This class is responsible for updating/inserting items in an inventory
 */
public class UpsertViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ImageAdapter.OnDeleteButtonClickListener {
    private Button add_tags_button;
    private Button upload_image_button;
    private Button submit_button;
    private Button cancel_button;
    private ImageButton scan_barcode_button;
    private ImageButton scan_serial_button;
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
    boolean isAdd = false;
    private ListView imageListView;
    private ImageAdapter imageAdapter;
    private ArrayList<String> imageUris = new ArrayList<>();
    private ArrayList<String> imageLinks = new ArrayList<>();
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
        upload_image_button = findViewById(R.id.upload_images_button);
        cancel_button = findViewById(R.id.cancel_button);
        scan_barcode_button = findViewById(R.id.scan_barcode_button);
        scan_serial_button = findViewById(R.id.serial_number_scanner);
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

        // sets image adapter to view image uploaded list
        imageListView = findViewById(R.id.image_listview);
        imageAdapter = new ImageAdapter(this, imageUris);
        imageListView.setAdapter(imageAdapter);
        imageAdapter.setOnDeleteButtonClickListener(this);

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

            imageUris = item.getImageLinks();
            imageAdapter = new ImageAdapter(this, imageUris);
            imageListView.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();
            justifyListViewHeightBasedOnChildren();
            imageAdapter.setOnDeleteButtonClickListener(this);

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
//            imageAdapter.notifyDataSetChanged();
//            justifyListViewHeightBasedOnChildren();
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

        scan_serial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanSerialNumber();
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
         * Creates a pop-up dialog for user to upload pictures by using their camera or
         * from the gallery
         */
        upload_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageDialog();
            }
        });

        /**
         * Submits the form if data is valid and will either add item to inventory list or
         * view item if user wants to edit item
         */
        submit_button.setOnClickListener(new View.OnClickListener() {
            //TODO: refactor
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

                    // set the image URLs and upload images to Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");
                    String itemId = description + "-" + dateFormatted + "-" + estimated_cost; //assumes the desc-date-value is unique to this item

                    Item upsert_item = new Item(dateFormatted, description, make, model, estimated_cost, serial_number, comment, tags, imageUris);
                    CountDownLatch latch = new CountDownLatch(imageUris.size());

                    for (int i = 0; i < imageUris.size(); i++) {
                        int temp = i + 1;
                        boolean is_uploaded = imageUris.get(i).startsWith("http");
                        if (!is_uploaded){ // only upload images that have not already been uploaded, unless the
                            String date = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
                            String pathString =  date + temp + ".jpg";
                            UploadTask uploadTask = storageRef.child(itemId).child(pathString).putFile(Uri.parse(imageUris.get(i)));

                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                storageRef.child(itemId).child(pathString).getDownloadUrl().addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    imageLinks.add(downloadUrl);
                                    latch.countDown();
                                });
                            }).addOnFailureListener(exception -> {
                                exception.printStackTrace();
                                latch.countDown();
                            });
                        } else {
                            imageLinks.add(imageUris.get(i));
                            latch.countDown();
                        }
                    }

                    new Thread(() -> {
                        try {
                            latch.await(); // Wait for all uploads to complete
                            // image links contain URLs
                            if (isAdd) {
                                upsert_item.setImageLinks(imageLinks);
                                addItemToFirestore(upsert_item);
                            } else {
                                item.setImageLinks(imageLinks);
                                item.setDescription(description);
                                item.setMake(make);
                                item.setModel(model);
                                item.setEstimatedValue(estimated_cost);
                                item.setSerialNumber(serial_number);
                                item.setComment(comment);
                                item.setItemTags(tags);

                                FirestoreDB.editItemFromFirestore(username, inventory, item);
                                intent.putExtra("editedItem", item);
                                setResult(RESULT_OK, intent); // sends item back to parent activity
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
                if (imageUris.size()>0) {
                    if (isAdd) {
                        Toast.makeText(UpsertViewActivity.this, "Item being added...", Toast.LENGTH_LONG).show();
                    } else {
                        item.setDescription(description);
                        item.setMake(make);
                        item.setModel(model);
                        item.setEstimatedValue(estimated_cost);
                        item.setSerialNumber(serial_number);
                        item.setComment(comment);
                        item.setItemTags(tags);

                        FirestoreDB.editItemFromFirestore(username, inventory, item);
                        intent.putExtra("editedItem", item);
                        Toast.makeText(UpsertViewActivity.this, "Item being updated!", Toast.LENGTH_LONG).show();
                    }
                }
                submit_button.setEnabled(false);
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
        CountDownLatch latch = new CountDownLatch(1);
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getItemsRef(username, inventory).add(item).addOnSuccessListener(documentReference -> {
                // Get the generated ID and store it in the item
                String generatedId = documentReference.getId();
                item.setId(generatedId);

                // Update the item in Firestore with its ID
                FirestoreDB.getItemsRef(username, inventory).document(generatedId).set(item);
                Toast.makeText(UpsertViewActivity.this, "Item successfully added!", Toast.LENGTH_SHORT).show();
                latch.countDown();
            }).addOnFailureListener(e -> {
                // Handle failure
                e.printStackTrace();
                latch.countDown();
            });
        }

        new Thread(() -> {
            try {
                // Wait for item to be added to Firestore
                latch.await();

                // Update intent and finish activity here
                Intent intent = new Intent();
                intent.putExtra("item_to_add", item);
                setResult(RESULT_OK, intent);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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

    /**
     * Launches activity to take a picture, crop it, and serial number from the cropped image
     */
    public void scanSerialNumber() {
        ImagePicker.with(UpsertViewActivity.this)
                .cameraOnly()
                .crop(16f, 3f)
                .start(ActivityCodes.SCAN_SERIAL_NUMBER.getRequestCode());
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


    /**
     * Creates a dialog prompting user to select from where they want to upload their pictures from
     */
    private void chooseImageDialog() {
        // sets background to be grey
        final View greyBack = findViewById(R.id.fadeBackgroundUpsert);
        greyBack.setVisibility(View.VISIBLE);

        //initialize bottom dialog to display options to upload image
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.select_image_dialog_layout);

        LinearLayout pickCamera = bottomSheetDialog.findViewById(R.id.take_photo);
        LinearLayout pickGallery = bottomSheetDialog.findViewById(R.id.select_from_gallery);
        Button cancelDialog = bottomSheetDialog.findViewById(R.id.image_select_cancel_button);

        // take picture using camera
        pickCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UpsertViewActivity.this)
                        .cameraOnly()
                        .start(ActivityCodes.CAMERA_PHOTO.getRequestCode());
                bottomSheetDialog.dismiss();
            }
        });

        // select picture from gallery
        pickGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(UpsertViewActivity.this)
                        .galleryOnly()
                        .start(ActivityCodes.GALLERY_PHOTO.getRequestCode());
                bottomSheetDialog.dismiss();
            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        // displays default upsert view with fields
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                greyBack.setVisibility(View.GONE);
            }
        });
        bottomSheetDialog.show();
    }

    /**
     * Handles the result of activities that were started for a result.
     *
     * @param requestCode The request code that was used to start the activity.
     * @param resultCode  The result code returned by the activity.
     * @param data        The data returned by the activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ActivityCodes.CAMERA_PHOTO.getRequestCode()) {
            Uri uri = data.getData();
            if (uri != null) {
                imageUris.add(uri.toString());
                imageAdapter.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren();
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == ActivityCodes.GALLERY_PHOTO.getRequestCode()) {
            Uri uri = data.getData();
            if (uri != null) {
                imageUris.add(uri.toString());
                imageAdapter.notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren();
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == ActivityCodes.SCAN_SERIAL_NUMBER.getRequestCode()) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    textDetector(uri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Updates the list view to make the height such that all the images can be seen instead of
     * having the list scrollable
     */
    public void justifyListViewHeightBasedOnChildren() {
        if (imageAdapter == null) {
            return;
        }
        int totalHeight = 0;
        // incrementing total height of list view by adding heights of each item
        for (int i = 0; i < imageAdapter.getCount(); i++) {
            View listItem = imageAdapter.getView(i, null, imageListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        // changing layout of list view to reflect new height
        ViewGroup.LayoutParams par = imageListView.getLayoutParams();
        par.height = totalHeight + (imageListView.getDividerHeight() * (imageAdapter.getCount() - 1));
        imageListView.setLayoutParams(par);
        imageListView.requestLayout();
    }

    /**
     * deletes an image from the listed images
     * called when the user clicks on the delete button of a given image
     */
    @Override
    public void onDeleteButtonClick(int position) {
        imageUris.remove(position);
        imageAdapter.notifyDataSetChanged();
        justifyListViewHeightBasedOnChildren();
        position += 1;
        Toast.makeText(this, "Image " + position + " deleted", Toast.LENGTH_SHORT).show();
    }

    /**
     *  reads all the text from an image
     *  called once the user sends a cropped picture of just the serial number
     */
    public void textDetector(Uri uri) throws IOException {
        // initialize the tools
        TextRecognizer serial_number_reader = new TextRecognizer.Builder(this).build();
        Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        Frame frame = new Frame.Builder().setBitmap(image).build();
        StringBuilder serialNum = new StringBuilder();

        // read the text from the bitmap image using the text recognizer - construct a string from it
        SparseArray<TextBlock> temp_arr = serial_number_reader.detect(frame);
        for (int i = 0; i <temp_arr.size(); i++) {
            TextBlock t_char = temp_arr.get(i);
            String chr = t_char.getValue();
            serialNum.append(chr);
        }
        serial_number_text.setText(serialNum);
    }
}