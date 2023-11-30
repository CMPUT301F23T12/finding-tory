package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finding_tory.Inventory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpsertInventoryViewActivity extends AppCompatActivity {
    private Button submit_button;
    private Button cancel_button;
    private String username;
    private Inventory inventory;
    private EditText inventory_text;
    private boolean isAdd = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsert_inventory_view);
        inventory_text = findViewById(R.id.inventoryNameText);
        submit_button = findViewById(R.id.submit_button);
        cancel_button = findViewById(R.id.cancel_button);

        Bundle extras = getIntent().getExtras();
        username = (String) extras.getSerializable("username");

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checks for error and makes sure required input is filled
                String inventory_name = inventory_text.getText().toString();

                if (inventory_name.equals("")) {
                    // TODO: display error message on screen
                    Toast toast = Toast.makeText(getApplicationContext(), "Inventory Name cannot be empty!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Intent intent = new Intent();
                    Inventory upsert_inventory = new Inventory(inventory_name);
                    intent.putExtra("inventory_to_add", upsert_inventory);

                    if (isAdd) {
                        addInventoryToFirestore(upsert_inventory);
                    } else {
                        // TODO: Handling editing an inventory
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
     * Adds an Inventory to Firestore inventories collection.
     *
     * @param inventory The Inventory to be added to Firestore.
     */
    private void addInventoryToFirestore(Inventory inventory) {
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getInventoriesRef(username).document(inventory.getInventoryName()).set(inventory).addOnSuccessListener(aVoid -> {
                // Item added successfully
                Toast.makeText(UpsertInventoryViewActivity.this, "Inventory added successfully!", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
