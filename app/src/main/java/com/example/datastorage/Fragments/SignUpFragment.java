package com.example.datastorage.Fragments;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.datastorage.Activities.DashboardActivity;
import com.example.datastorage.Models.UserProfile;
import com.example.datastorage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Button signup;
    TextInputEditText signUpUserName, signUpPassword, signUpPhone, signUpEmail;
    ProgressBar signUpProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signup = view.findViewById(R.id.signUpButton);
        signUpUserName = view.findViewById(R.id.signUpUserName);
        signUpPassword = view.findViewById(R.id.signUpPassword);
        signUpPhone = view.findViewById(R.id.signUpPhone);
        signUpEmail = view.findViewById(R.id.signUpEmail);
        signUpProgressBar = view.findViewById(R.id.signUpProgressbar);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister();
            }
        });

        return view;
    }

    private void userRegister() {
        String username = signUpUserName.getText().toString().trim();
        String email = signUpEmail.getText().toString().trim();
        String phone = signUpPhone.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();

        if (username.isEmpty()) {
            signUpUserName.setError("Enter a valid full Name", null);
            signUpUserName.requestFocus();
            return;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmail.setError("Enter a valid email address", null);
            signUpEmail.requestFocus();
            return;
        }
        if (phone.isEmpty() || phone.length() < 11) {
            signUpPhone.setError("Enter a valid mobile number", null);
            signUpPhone.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            signUpPassword.setError("Enter password", null);
            signUpPassword.requestFocus();
            return;
        }

        signUpProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signUpProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    saveUserData(username, phone);
                    getActivity().finish();
                    Toast.makeText(getActivity().getApplicationContext(), "User Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getActivity().getApplicationContext(), "User is already Registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "User Registration Fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //Save User data in firebase realtime database
    private void saveUserData(String username, String phone) {
        String uid = mAuth.getCurrentUser().getUid();
        UserProfile userProfile = new UserProfile(username, phone);
        databaseReference.child(uid).setValue(userProfile);
    }
}