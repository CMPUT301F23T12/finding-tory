package com.example.finding_tory;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InternalStorageManager {
    private static final String FILENAME = "user_data.txt";
    private Context context;

    public InternalStorageManager(Context context) {
        this.context = context;
    }

    /**
     * Saves a username to the internal storage.
     *
     * @param username The username string to be saved.
     * @throws IOException If an I/O error occurs while writing the username to the file.
     */
    public void saveUsername(String username) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(username.getBytes());
        fos.close();
    }

    /**
     * Retrieves the username from internal storage.
     *
     * @return The username as a string.
     * @throws IOException If an I/O error occurs while reading the username from the file.
     */
    public String getUsername() throws IOException {
        FileInputStream fis = context.openFileInput(FILENAME);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        fis.close();
        return sb.toString();
    }
}
