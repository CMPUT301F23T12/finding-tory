package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

                if (!(username.equals("") && name.equals("") && password.equals("") && confirmPassword.equals(""))) {
                    if (password.equals(confirmPassword)) {
                        registerUser(username, name, password);
                    } else {
                        passwordEditText.setText("");
                        confirmPwdEditText.setText("");
                        Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "You cannot have empty fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Registers a new user by checking if the username is available and adding the user to Firestore.
     *
     * @param username The desired username for the new user.
     * @param name     The name of the new user.
     * @param password The password for the new user.
     */
    public void registerUser(String username, String name, String password) {
        if (!FirestoreDB.isDebugMode()) {
            FirestoreDB.getUsersRef().whereEqualTo("username", username).get().addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // Username already exists
                    usernameEditText.setText("");
                    Toast.makeText(RegisterActivity.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                } else {
                    // Username is available, we can add the user to collection
                    User user = new User(username, name, password);
                    FirestoreDB.getUsersRef().document(username).set(user).addOnSuccessListener(documentReference -> {
                        Intent intent = new Intent(RegisterActivity.this, LedgerViewActivity.class);
                        intent.putExtra("username", user.getUsername());
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
            intent.putExtra("username", user.getUsername());
            startActivity(intent);
            Toast.makeText(RegisterActivity.this, "Successfully registered!", Toast.LENGTH_SHORT).show();
        }
    }
}
