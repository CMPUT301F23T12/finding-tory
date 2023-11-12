package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * RegisterActivity is an AppCompatActivity that provides a user interface for new account registration.
 * It allows users to enter new information to create an account under the system.
 * <p>
 * This activity includes new user credentials and initiates a LedgerViewActivity on successful registration.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText confirmPwdEditText;

    /**
     * Initializes the activity, sets up the user interface, and prepares event listeners for buttons.
     * User inputs for username info are captured and processed in this method.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize and cache the EditTexts for user info
        usernameEditText = findViewById(R.id.edit_text_username);
        nameEditText = findViewById(R.id.edit_text_name);
        passwordEditText = findViewById(R.id.edit_text_password);
        confirmPwdEditText = findViewById(R.id.edit_text_confirm_password);

        // initialize registration button, set click listener
        Button registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(usernameEditText.getText());
                String name = String.valueOf(nameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                String confirmPassword = String.valueOf(confirmPwdEditText.getText());
                // TODO handle new user registration
                addDummyData();
//                registerUser("","","");
//                if (!(username.equals("") && name.equals("") && password.equals("") && confirmPassword.equals(""))) {
//                    if (password.equals(confirmPassword)) {
//                        registerUser(username, name, password);
//                    } else {
//                        passwordEditText.setText("");
//                        confirmPwdEditText.setText("");
//                        Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(RegisterActivity.this, "You cannot have empty fields!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public void registerUser(String username, String name, String password) {
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getUsersRef().whereEqualTo("username", username).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Username already exists
                    Toast.makeText(RegisterActivity.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                } else {
                    // Username is available, you can add the user to Firestore here
                    User user = new User(username, name, password);
                    FirestoreDB.getUsersRef().document(username).set(user)
                            .addOnSuccessListener(documentReference -> {
                                Intent intent = new Intent(RegisterActivity.this, LedgerViewActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                // Error occurred while adding user
                                Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            });
        } else {
            User user = new User(username, name, password);
            Intent intent = new Intent(RegisterActivity.this, LedgerViewActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addDummyData() {
        // Create a new User
        String username = "abc";
        Date dateFormatted = null;
        try {
            dateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User user = new User(username, "kevin", "123");

        // Create an Inventory
        Inventory inventory = new Inventory("Garage Inventory");
        addUserToFirestore(user);
        // Create Items
        Item item1 = new Item(dateFormatted, "item1", "make1", "model1", 10.99f, "", "Comment", new ArrayList<>());
        Item item2 = new Item(dateFormatted, "item2", "make2", "model2", 10.99f, "", "Comment", new ArrayList<>());
//        addInventoryToFirestore(username, inventory);

        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.setInventoryTotalItems();
        inventory.calculateValue();
        addInventoryToFirestore(username, inventory);

        // Save the inventory and items to Firestore under the user's subcollection

        addItemsToFirestore(username, inventory, Arrays.asList(item1, item2));
    }

    public void addUserToFirestore(User user) {
        FirestoreDB.getUsersRef().document(user.getUsername())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Log success or show a toast
                })
                .addOnFailureListener(e -> {
                    // Log error or show a toast
                });
    }

    public void addInventoryToFirestore(String username, Inventory inventory) {
        FirestoreDB.getInventoriesRef(username).document(inventory.getInventoryName()).set(inventory);
    }

    public void addItemsToFirestore(String username, Inventory inventory, List<Item> items) {
        // Get a reference to the specific inventory's subcollection for items
        CollectionReference itemsRef = FirestoreDB.getInventoriesRef(username).document(inventory.getInventoryName()).collection("Items");

        // Iterate over the list of items and add each to Firestore
        for (Item item : items) {
            // You can use the item name as a document ID, or let Firestore generate an ID
            itemsRef.document(item.getDescription()).set(item)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful addition, e.g., logging or showing a toast
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors here, e.g., logging or showing a toast
                    });
        }
    }
}
