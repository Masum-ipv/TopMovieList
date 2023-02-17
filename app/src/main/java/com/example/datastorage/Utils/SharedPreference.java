package com.example.datastorage.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;


public class SharedPreference extends AppCompatActivity {

    Context context;
    String sharedPrefDb = "userData";

    public SharedPreference(Context context) {
        this.context = context;
    }

    public void SaveSharedPreference(String[] key, String[] value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefDb, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < key.length; i++) {
            editor.putString(key[i], value[i]);
        }
        editor.apply();
    }

    public String GetSharedPreferenceData(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPrefDb, Context.MODE_PRIVATE);
        String result = "";
        if (sharedPref.contains(key)) {
            result = sharedPref.getString(key, result);
        }
        return result;
    }
}
