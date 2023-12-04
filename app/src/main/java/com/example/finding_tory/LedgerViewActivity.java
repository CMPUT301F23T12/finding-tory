package com.example.finding_tory;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

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
    private final Ledger ledger = Ledger.getInstance();
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

        loadUser();
        if (ledger.getUser().getUsername() == null || ledger.getUser().getUsername().isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, ActivityCodes.LOGIN_USER.getRequestCode());
        }
        // once the login/registration activity is done, we have a chance to launch the ledger fragment
        if (ledger.getUser().getUsername() != null) {
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
                if (ledger.getUser().getUsername() != null) {
                    saveData(ledger.getUser());
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
        LedgerFragment ledgerFragment = new LedgerFragment();

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
                saveData(null);
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
     * Saves the user's info to the internal storage for future reference.
     *
     * @param user The user to be saved.
     */
    public void saveData(User user) {
        try {
            internalStorageManager.saveUser(user);
        } catch (IOException ignored) {

        }
    }

    /**
     * Loads the user's info from the internal storage.
     */
    public void loadUser() {
        try {
            ledger.setUser(internalStorageManager.getUser());
            // Use the username
        } catch (IOException e) {
            ledger.setUser(null);
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
