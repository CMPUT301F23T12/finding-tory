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

/**
 * Activity for creating an inventory.
 * This activity provides a user interface to add a new inventory.
 * It includes input fields for the inventory details and buttons to submit or cancel the operation.
 */
public class UpsertInventoryViewActivity extends AppCompatActivity {
    private Button submit_button;
    private Button cancel_button;
    private Button delete_button;
    private String username;
    private Inventory inventory;
    private EditText inventory_text;
    private String inventory_name;
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
        delete_button = findViewById(R.id.delete_button);

        Bundle extras = getIntent().getExtras();
        inventory = null;
        // if no data is sent through intent, then user wants to add an item
        if (extras != null) {
            username = (String) extras.getSerializable("username");
            inventory = (Inventory) extras.getSerializable("inventory");

            // if item != null then we are editing an item
            if (inventory == null) {
                isAdd = true;
                delete_button.setVisibility(View.GONE);
            } else {
                inventory_text.setText(inventory.getInventoryName());
            }
        }
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventory_name = inventory_text.getText().toString();
                if (inventory_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Inventory Name cannot be empty!", Toast.LENGTH_LONG).show();
                } else {
                    if (isAdd) {
                        addInventoryToFirestore(new Inventory(inventory_name));
                    } else {
                        inventory.setName(inventory_name);
                        editInventoryFromFirestore(inventory);
                    }
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

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show Delete Confirmation Fragment
                DeleteConfirmationFragment deleteDialog = new DeleteConfirmationFragment();

                // Set the listener to know when the dialog is dismissed
                deleteDialog.setDeleteDialogListener(new DeleteConfirmationFragment.DeleteDialogListener() {
                    @Override
                    public void onDialogDismissed() {
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                    @Override
                    public void onDeleteConfirmed() {
                        deleteInventoryFromFirestore(inventory);
                        Intent intent = new Intent();
                        intent.putExtra("inventory_to_delete", inventory);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                deleteDialog.show(getSupportFragmentManager(), "DELETE_ITEM");
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
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getInventoriesRef(username).add(inventory).addOnSuccessListener(documentReference -> {
                // Get the generated ID and store it in the inventory
                String generatedId = documentReference.getId();
                inventory.setId(generatedId);

                // Update the inventory in Firestore with its ID
                FirestoreDB.getInventoriesRef(username).document(generatedId).set(inventory);
                Toast.makeText(UpsertInventoryViewActivity.this, "Inventory successfully added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("inventory_to_add", inventory);
                setResult(RESULT_OK, intent);
                finish();
            }).addOnFailureListener(e -> {
                // Handle failure
                e.printStackTrace();
            });
        }
    }

    /**
     * Adds an Inventory object to Firestore.
     * This method is called after successful validation of the inventory name.
     * On successful addition, it sets the result of the activity and finishes it.
     *
     * @param inventory The Inventory object to be added to Firestore.
     */
    private void editInventoryFromFirestore(Inventory inventory) {
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getInventoriesRef(username).document(inventory.getId()).set(inventory).addOnSuccessListener(documentReference -> {
                Toast.makeText(UpsertInventoryViewActivity.this, "Inventory name successfully updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("inventory_to_add", inventory);
                setResult(RESULT_OK, intent);
                finish();
            }).addOnFailureListener(e -> {
                // Handle failure
                e.printStackTrace();
            });
        }
    }


    /**
     * Delete all the items from the inventory and then deletes the inventory.
     *
     * @param inventory The Inventory object to be delete from Firestore.
     */
    private void deleteInventoryFromFirestore(Inventory inventory) {
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getItemsRef(username, inventory).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        FirestoreDB.getItemsRef(username, inventory).document(document.getId()).delete();
                    }

                    // After deleting all items, delete the inventory
                    FirestoreDB.getInventoriesRef(username).document(inventory.getId()).delete().addOnSuccessListener(aVoid -> {
                        Toast.makeText(UpsertInventoryViewActivity.this, "Inventory and all items deleted successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }
}
