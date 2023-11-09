package com.example.finding_tory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;


    /**
     * Initializes the instance variables and bindings associated with this activity on creation.
     *
     * @param savedInstanceState
     *          possible default layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize and cache the EditTexts for user info
        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);

        // initialize+cache buttons, set click listeners
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO implement new user registration
                Snackbar.make(v, "User registration coming soon!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch a new Ledger view activity for this user
                // TODO actually get the user info at login. for now it doesn't matter

                Intent intent = new Intent(LoginActivity.this, LedgerViewActivity.class);
                intent.putExtra("user", usernameEditText.getText());
                startActivity(intent);
            }
        });
    }
}