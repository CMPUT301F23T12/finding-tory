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

    public void saveUsername(String username) throws IOException {
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(username.getBytes());
        fos.close();
    }

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
