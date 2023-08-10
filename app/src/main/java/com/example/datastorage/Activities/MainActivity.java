package com.example.datastorage.Activities;

import static com.example.datastorage.Utils.Helper.SHARED_PREF_KEY;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datastorage.R;
import com.example.datastorage.Utils.SharedPreference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Hide title bar from signIn and signup screen.

        //IF user already login once today, Just show a toast
        SharedPreference sharedPreference = new SharedPreference(this);
        String data = sharedPreference.GetSharedPreferenceData(SHARED_PREF_KEY);
        if (!Objects.equals(data, "")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date formattedDate = sdf.parse(data);
                result = DateUtils.isToday(formattedDate.getTime());
            } catch (ParseException e) {
            }

        }
        if (!result) {
            Toast.makeText(getApplicationContext(), "Welcome Back\nHave a good day", Toast.LENGTH_SHORT).show();
        }
    }
}