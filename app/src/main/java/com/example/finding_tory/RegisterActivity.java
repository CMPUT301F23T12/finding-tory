package com.example.finding_tory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/**
 * RegisterActivity is an AppCompatActivity that provides a user interface for new account registration.
 * It allows users to enter new information to create an account under the system.
 *
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
                // TODO handle new user registration
                Snackbar.make(v, "Registration coming soon!", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
