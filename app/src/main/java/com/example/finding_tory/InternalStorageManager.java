package com.example.finding_tory;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A class for managing internal storage operations in an Android application.
 * This class provides functionality to save and retrieve user data, specifically usernames,
 * to and from internal storage. It utilizes Android's file system to store data in a private mode,
 * ensuring the data is accessible only to the application.
 */
public class InternalStorageManager {
    private static final String FILENAME = "user_data.txt";
    private Context context;

    public InternalStorageManager(Context context) {
        this.context = context;
    }

    /**
     * Saves a logged-in user to the internal storage.
     *
     * @param user          The user object to be saved.
     * @throws IOException  If an I/O error occurs while writing the username to the file.
     */
    public void saveUser(User user) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        System.out.println("THERE");
        if (user == null || user.getUsername() == null) {
            user = new User("", "", "");
            System.out.println("HERE");
        }
        System.out.println(user.getUsername());
        fos.write(user.getUsername().concat("\n").getBytes());
        fos.write(user.getName().concat("\n").getBytes());
        fos.write(user.getPassword().concat("\n").getBytes());
        fos.close();
    }

    /**
     * Retrieves the username from internal storage.
     *
     * @return The username as a string.
     * @throws IOException If an I/O error occurs while reading the username from the file.
     */
    public User getUser() throws IOException {
        FileInputStream fis = context.openFileInput(FILENAME);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        String username = bufferedReader.readLine();
        String name = bufferedReader.readLine();
        String password = bufferedReader.readLine();
        fis.close();
        return new User(username, name, password);
    }
}
