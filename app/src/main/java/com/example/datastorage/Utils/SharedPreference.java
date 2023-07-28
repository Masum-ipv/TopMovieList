package com.example.datastorage.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;


public class SharedPreference extends AppCompatActivity {

    Context context;
    String sharedPrefDb = "userData";

    public SharedPreference(Context context) {
        this.context = context;
    }

    public void SaveSharedPreference(String key, String value) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefDb, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
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
