package com.example.datastorage.Activities;

import static com.example.datastorage.Utils.Helper.SHARED_PREF_KEY;
import static com.example.datastorage.Utils.Helper.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

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

        if (result) {
            Intent intent = new Intent(this, DashboardActivity.class);
            this.finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            Objects.requireNonNull(getSupportActionBar()).hide(); //Hide title bar from signIn and signup screen.
        }
    }
}