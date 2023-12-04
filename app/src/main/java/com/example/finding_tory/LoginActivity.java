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
import com.google.firebase.firestore.QuerySnapshot;

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

        // initialize and cache the EditTexts for user info
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);


        // initialize registration button, set click listener
        Button registerButton = findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, ActivityCodes.REGISTER_USER.getRequestCode());
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
                    loginUser("cq4", "123");
                } else {
                    loginUser(username, password);
                    //Snackbar.make(v, "Invalid user credentials. Please try again.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Handles the result of activities that were started for a result.
     *
     * @param requestCode The request code that was used to start the activity.
     * @param resultCode  The result code returned by the activity.
     * @param data        The data returned by the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityCodes.REGISTER_USER.getRequestCode()) {
            if (resultCode == RESULT_OK && data != null) {
                String LOGGEDIN_USER = data.getStringExtra("username");
                Intent resultIntent = getIntent();
                resultIntent.putExtra("username", LOGGEDIN_USER);
                setResult(RESULT_OK, resultIntent);
            }
            finish();
        }
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
        FirestoreDB.getUsersRef().whereEqualTo("username", username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    String storedPassword = queryDocumentSnapshots.getDocuments().get(0).getString("password");
                    String name = queryDocumentSnapshots.getDocuments().get(0).getString("name");
                    if (BCrypt.checkpw(enteredPassword, storedPassword)) {
                        Intent resultIntent = getIntent();
                        resultIntent.putExtra("username", username);
                        Ledger.getInstance().setUserNames(username, name);
                        setResult(RESULT_OK, resultIntent);
                        finish();
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
