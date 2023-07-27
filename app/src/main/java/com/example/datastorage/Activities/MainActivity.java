package com.example.datastorage.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datastorage.Fragments.SignInFragment;
import com.example.datastorage.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide(); //Hide title bar from signin and signup screen.
    }
}