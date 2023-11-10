package com.example.finding_tory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;


/**
 * LoginActivity is an AppCompatActivity that provides a user interface for login and new account registration.
 * It allows users to enter their username and password to log in or to navigate to the registration process.
 *
 * This activity includes validation for user credentials and initiates transitions to other activities based on user actions.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    /**
     * Initializes the activity, sets up the user interface, and prepares event listeners for buttons.
     * User inputs for username and password are captured and processed in this method.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize and cache the EditTexts for user info
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);

        // initialize registration button, set click listener
        Button registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO implement new user registration
                Snackbar.make(v, "User registration coming soon!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // initialize login button, set click listener
        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch a new Ledger view activity for this user
                // TODO validate real user info at login. for now we use mock credentials
                String username = String.valueOf(usernameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                passwordEditText.setText("");  // when we return we need to re-enter password

                if (!(username.equals("user") && password.equals("password"))) {
                    Snackbar.make(v, "Invalid user credentials. Please try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, LedgerViewActivity.class);
                intent.putExtra("user", username);
                startActivity(intent);
            }
        });
    }
}
