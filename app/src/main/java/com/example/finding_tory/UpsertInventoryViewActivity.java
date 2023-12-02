package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Activity for creating an inventory.
 * This activity provides a user interface to add a new inventory.
 * It includes input fields for the inventory details and buttons to submit or cancel the operation.
 */
public class UpsertInventoryViewActivity extends AppCompatActivity {
    private Button submit_button;
    private Button cancel_button;
    private String username;
    private Inventory inventory;
    private EditText inventory_text;
    private boolean isAdd = false;
    private ArrayList<String> inventoryNames;

    /**
     * Initializes the activity when starting
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upsert_inventory_view);
        inventory_text = findViewById(R.id.inventoryNameText);
        submit_button = findViewById(R.id.submit_button);
        cancel_button = findViewById(R.id.cancel_button);

        Bundle extras = getIntent().getExtras();
        inventory = null;
        // if no data is sent through intent, then user wants to add an item
        if (extras != null) {
            username = (String) extras.getSerializable("username");
            inventory = (Inventory) extras.getSerializable("inventoryName");

            // if item != null then we are editing an item
            if (inventory == null) {
                isAdd = true;
            } else {
                inventory_text.setText(inventory.getInventoryName());
            }
        }
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inventory_name = inventory_text.getText().toString();

                if (inventory_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Inventory Name cannot be empty!", Toast.LENGTH_LONG).show();
                } else {
                    validateInventoryName(inventory_name);
                    // Do not finish the activity here
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

    /**
     * Validates the given inventory name against the Firestore database to ensure uniqueness.
     * If the name is unique and valid, it proceeds to add the inventory to Firestore.
     * Otherwise, it displays a toast message indicating the name is not valid.
     *
     * @param inventoryName The inventory name to be validated.
     */
    private void validateInventoryName(String inventoryName) {
        FirestoreDB.getInventoriesRef(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isNameValid = true;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Inventory inv = document.toObject(Inventory.class);
                    if (Objects.equals(inv.getInventoryName(), inventoryName)) {
                        isNameValid = false;
                        break;
                    }
                }

                if (isNameValid) {
                    addInventoryToFirestore(new Inventory(inventoryName));
                } else {
                    Toast.makeText(getApplicationContext(), "Inventory Name cannot have the same name as an existing inventory!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Adds an Inventory object to Firestore.
     * This method is called after successful validation of the inventory name.
     * On successful addition, it sets the result of the activity and finishes it.
     *
     * @param inventory The Inventory object to be added to Firestore.
     */
    private void addInventoryToFirestore(Inventory inventory) {
        // Finish the activity only after successful addition
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getInventoriesRef(username).document(inventory.getInventoryName()).set(inventory).addOnSuccessListener(aVoid -> {
                Toast.makeText(UpsertInventoryViewActivity.this, "Inventory added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("inventory_to_add", inventory);
                setResult(RESULT_OK, intent);
                finish();
            });
        }
    }

}
