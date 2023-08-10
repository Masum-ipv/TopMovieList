package com.example.datastorage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datastorage.Models.UserModel;
import com.example.datastorage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private FirebaseUser currentUser;
    private final String USER_ID = "userId";
    private final String USER_NAME = "userName";
    private final String USER_Number = "userNumber";

    TextView fullName;
    TextInputEditText fullNameHome, emailHome, phoneHome;
    Button profileUpdate;
    ProgressBar profileProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Adding Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        profileUpdate = findViewById(R.id.profile_update);
        fullName = findViewById(R.id.full_name);
        fullNameHome = findViewById(R.id.full_name_home);
        emailHome = findViewById(R.id.homeEmail);
        phoneHome = findViewById(R.id.homePhone);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        profileProgressBar = findViewById(R.id.profileProgressbar);

        profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = currentUser.getUid();
                String userName = fullNameHome.getText().toString().trim();
                String userNumber = phoneHome.getText().toString().trim();

                UserModel user = UserModel.getInstance();
                user.setUserName(userName);
                user.setUserNumber(userNumber);

                if (userName.isEmpty()) {
                    fullNameHome.setError("Enter a valid full Name", null);
                    fullNameHome.requestFocus();
                    return;
                }
                if (userNumber.isEmpty() || userNumber.length() < 11) {
                    phoneHome.setError("Enter a valid mobile number", null);
                    phoneHome.requestFocus();
                    return;
                }
                collectionReference
                        .whereEqualTo(USER_ID, currentUser.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        collectionReference.document(document.getId()).set(user);
                                    }
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileProgressBar.setVisibility(View.VISIBLE);
        collectionReference
                .whereEqualTo(USER_ID, currentUser.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            for (QueryDocumentSnapshot snapshot : value) {
                                fullName.setText(snapshot.getString(USER_NAME));
                                fullNameHome.setText(snapshot.getString(USER_NAME));
                                phoneHome.setText(snapshot.getString(USER_Number));
                                emailHome.setText(currentUser.getEmail());
                                profileProgressBar.setVisibility(View.GONE);
                            }
                        }
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
            firebaseAuth.signOut();
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