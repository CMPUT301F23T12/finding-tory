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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;


/**
 * LoginActivity is an AppCompatActivity that provides a user interface for login and new account registration.
 * It allows users to enter their username and password to log in or to navigate to the registration process.
 * <p>
 * This activity includes validation for user credentials and initiates transitions to other activities based on user actions.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String STORED_USER = "text";

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

        String storedUsername = loadData();
        if (!storedUsername.isEmpty()) {
            loginUser(storedUsername, "", true);
        }

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
                String username = String.valueOf(usernameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                passwordEditText.setText("");

                // TODO remove mock credentials
                if (username.equals("") && password.equals("")) {
                    loginUser("abc", "123", false);
                } else {
                    loginUser(username, password, false);
                    //Snackbar.make(v, "Invalid user credentials. Please try again.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void saveData(String usrname) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(STORED_USER, usrname);
        editor.apply();
    }

    public String loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        System.out.println("retrieved: " + sharedPreferences.getString(STORED_USER, ""));
        // TODO able to store the data, but logout isn't working
        return sharedPreferences.getString(STORED_USER, "");
    }

    /**
     * Login the user with the provided username and password.
     * Communicates with FirestoreDB to authenticate the user and, upon success,
     * transitions to the LedgerViewActivity.
     *
     * @param username        The username entered by the user.
     * @param enteredPassword The password entered by the user.
     */
    public void loginUser(final String username, final String enteredPassword, boolean bypass) {
        FirestoreDB.getUsersRef().whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot userSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                    // Retrieve the user object from the Firestore document
//                    User user = userSnapshot.toObject(User.class);

                    String storedPassword = queryDocumentSnapshots.getDocuments().get(0).getString("password");
                    if (enteredPassword.equals(storedPassword) || bypass) {
                        Intent intent = new Intent(LoginActivity.this, LedgerViewActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        saveData(username);
                    } else {
                        Snackbar.make(usernameEditText, "Invalid password. Please try again.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
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
