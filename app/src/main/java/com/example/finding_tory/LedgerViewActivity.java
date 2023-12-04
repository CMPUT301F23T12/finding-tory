package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.finding_tory.databinding.ActivityLedgerViewBinding;
import com.example.finding_tory.ui.ledger.LedgerFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

/**
 * LedgerViewActivity is an AppCompatActivity that manages the main user interface for the application.
 * It sets up a DrawerLayout with a NavigationView for navigating between different sections of the app like ledger and profile.
 * <p>
 * This activity handles the setup of the app bar and navigation components and manages navigation between different fragments.
 */
public class LedgerViewActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLedgerViewBinding binding;
    private InternalStorageManager internalStorageManager;
    private String AUTH_USER = "";
    private LedgerViewActivity currentViewContext;

    /**
     * Initializes the activity, sets up the navigation drawer and navigation components.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentViewContext = this;
        internalStorageManager = new InternalStorageManager(this);

        binding = ActivityLedgerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Welcome!");

        AUTH_USER = loadData();
        if (AUTH_USER.equals("")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, ActivityCodes.LOGIN_USER.getRequestCode());
        }
        if (!AUTH_USER.equals("")) {
            setupLedgerView();
        }
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

        if (requestCode == ActivityCodes.LOGIN_USER.getRequestCode()) {
            if (resultCode == RESULT_OK && data != null) {
                AUTH_USER = data.getStringExtra("username");
                if (!AUTH_USER.equals("")) {
                    saveData(AUTH_USER);
                    setupLedgerView();
                }
            } else {
                // Handle when the back is pressed
                finish();
            }
        }
    }

    /**
     * Sets up the ledger's UI, navigation, and event listeners.
     * It also initializes the LedgerFragment with the provided user info.
     */
    private void setupLedgerView() {
        LedgerFragment ledgerFragment = LedgerFragment.newInstance(AUTH_USER);

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_ledger, R.id.nav_profile).setOpenableLayout(drawer).build();

        // Use the NavController to navigate to the LedgerFragment
        // initialize the navigation bar functionality
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_ledger, ledgerFragment.getArguments());
        NavigationUI.setupWithNavController(navigationView, navController);

        // bind the logout "button" to end this activity
        LinearLayout logoutLayout = findViewById(R.id.logout_layout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData("");
                Intent intent = new Intent(currentViewContext, LoginActivity.class);
                startActivityForResult(intent, ActivityCodes.LOGIN_USER.getRequestCode());
            }
        });

        // override back button behaviour to toggle drawer instead of finish()
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isOpen()) drawer.close();
                else drawer.open();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Saves the user's username to the internal storage for future reference.
     *
     * @param usrname The username to be saved.
     */
    public void saveData(String usrname) {
        try {
            internalStorageManager.saveUsername(usrname);
        } catch (IOException ignored) {

        }
    }

    /**
     * Loads the user's username from the internal storage.
     *
     * @return The user's saved username, or an empty string if it couldn't be loaded.
     */
    public String loadData() {
        try {
            return internalStorageManager.getUsername();
            // Use the username
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Initializes the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which the items are placed.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This method is called whenever the user chooses to navigate up within the application's activity hierarchy from the action bar.
     *
     * @return true if Up navigation completed successfully and this Activity was finished, false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
