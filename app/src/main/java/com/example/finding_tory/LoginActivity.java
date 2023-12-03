package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.mindrot.jbcrypt.BCrypt;


/**
 * LoginActivity is an AppCompatActivity that provides a user interface for login and new account registration.
 * It allows users to enter their username and password to log in or to navigate to the registration process.
 * <p>
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

        // initialize Firebase Storage
        FirebaseApp.initializeApp(this);
        // initialize Picasso for loading images from the internet
        Picasso.setSingletonInstance(new Picasso.Builder(getApplicationContext()).build());

        // initialize and cache the EditTexts for user info
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);

        // initialize registration button, set click listener
        Button registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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

                if ((username.equals("") && password.equals("")) {
                     loginUser("cq4", "123");
                } else {
                    loginUser(username, password);
                    // Snackbar.make(v, "Invalid user credentials. Please try again.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Login the user with the provided username and password.
     * Communicates with FirestoreDB to authenticate the user and, upon success,
     * transitions to the LedgerViewActivity.
     *
     * @param username        The username entered by the user.
     * @param enteredPassword The password entered by the user.
     */
    public void loginUser(final String username, final String enteredPassword) {
        // Retrieve the user object from the Firestore document
        FirestoreDB.getUsersRef().whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot userSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                    // Retrieve the user object from the Firestore document
                    User user = userSnapshot.toObject(User.class);

                    // User with the provided username exists, get the stored password from the query result
                    String storedPassword = queryDocumentSnapshots.getDocuments().get(0).getString("password");
                    if (BCrypt.checkpw(enteredPassword, storedPassword)) {
                        // Start the LedgerViewActivity for the validated user
                        Intent intent = new Intent(LoginActivity.this, LedgerViewActivity.class);
                        intent.putExtra("username", user.getUsername());
                        startActivity(intent);
                    } else {
                        // Password is incorrect
                        Snackbar.make(usernameEditText, "Invalid password. Please try again.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // User with the provided username doesn't exist
                    Snackbar.make(usernameEditText, "User not found. Please try again.", Snackbar.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(usernameEditText, "Failed to check user credentials. Please try again.", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
