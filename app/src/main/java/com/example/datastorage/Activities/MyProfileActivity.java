package com.example.datastorage.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datastorage.Models.UserProfile;
import com.example.datastorage.R;
import com.example.datastorage.Utils.SharedPreference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    TextView fullName, lastLogin;
    TextInputEditText fullNameHome, emailHome, phoneHome;
    Button profileUpdate;
    ProgressBar profileProgressBar;
    UserProfile userProfile = new UserProfile();
    SharedPreference sharedPreference = new SharedPreference(this);
    String sharedPrefKey = "loginTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Adding Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profileUpdate = findViewById(R.id.profile_update);
        fullName = findViewById(R.id.full_name);
        lastLogin = findViewById(R.id.last_login);
        fullNameHome = findViewById(R.id.full_name_home);
        emailHome = findViewById(R.id.homeEmail);
        phoneHome = findViewById(R.id.homePhone);
        mAuth = FirebaseAuth.getInstance();
        profileProgressBar = findViewById(R.id.profileProgressbar);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String data = sharedPreference.GetSharedPreferenceData(sharedPrefKey);
        lastLogin.setText("Currently Watching: " + data);
        setDataFirebase();

        profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = mAuth.getCurrentUser().getUid();
                String username = fullNameHome.getText().toString().trim();
                String phone = phoneHome.getText().toString().trim();

                if (username.isEmpty()) {
                    fullNameHome.setError("Enter a valid full Name", null);
                    fullNameHome.requestFocus();
                    return;
                }
                if (phone.isEmpty() || phone.length() < 11) {
                    phoneHome.setError("Enter a valid mobile number", null);
                    phoneHome.requestFocus();
                    return;
                }

                UserProfile userProfile = new UserProfile(username, phone);
                databaseReference.child(uid).setValue(userProfile);
            }
        });
    }

    private void setDataFirebase() {
        profileProgressBar.setVisibility(View.VISIBLE);
        String uid = mAuth.getCurrentUser().getUid();
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(UserProfile.class);
                fullName.setText(userProfile.getUsername());
                fullNameHome.setText(userProfile.getUsername());
                phoneHome.setText(userProfile.getPhone());
                emailHome.setText(mAuth.getCurrentUser().getEmail());
                profileProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to Load User Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signOutMenuId) {
            FirebaseAuth.getInstance().signOut();
            this.finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) { //Adding Back button listener
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}