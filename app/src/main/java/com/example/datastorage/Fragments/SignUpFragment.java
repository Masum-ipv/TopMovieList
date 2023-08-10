package com.example.datastorage.Fragments;

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
import androidx.fragment.app.Fragment;

import com.example.datastorage.Activities.DashboardActivity;
import com.example.datastorage.Models.UserModel;
import com.example.datastorage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpFragment extends Fragment {

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    // Firebase Connection
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Users");
    Button signup;
    TextInputEditText signUpUserName, signUpPassword, signUpPhone, signUpEmail;
    ProgressBar signUpProgressBar;
    private final String USER_ID = "userId";
    private final String USER_NAME = "userName";
    private final String USER_Number = "userNumber";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        signup = view.findViewById(R.id.signUpButton);
        signUpUserName = view.findViewById(R.id.signUpUserName);
        signUpPassword = view.findViewById(R.id.signUpPassword);
        signUpPhone = view.findViewById(R.id.signUpPhone);
        signUpEmail = view.findViewById(R.id.signUpEmail);
        signUpProgressBar = view.findViewById(R.id.signUpProgressbar);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    //User Already logged in
                } else {
                    //No user yet!
                }
            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        return view;
    }

    private void registerUser() {
        String userName = signUpUserName.getText().toString().trim();
        String email = signUpEmail.getText().toString().trim();
        String phone = signUpPhone.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();

        if (userName.isEmpty()) {
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
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {
                                final String currentUserId = currentUser.getUid();
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put(USER_ID, currentUserId);
                                userObj.put(USER_NAME, userName);
                                userObj.put(USER_Number, phone);

                                //Adding User to firebase
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Objects.requireNonNull(task.getResult().exists())) {
                                                                    signUpProgressBar.setVisibility(View.GONE);
                                                                    String name = task.getResult().getString(USER_NAME);
                                                                    // Global Journal User
                                                                    UserModel userModel = UserModel.getInstance();
                                                                    userModel.setUserId(currentUserId);
                                                                    userModel.setUserName(name);

                                                                    getActivity().finish();
                                                                    Toast.makeText(getActivity().getApplicationContext(),
                                                                            "User Registration Successful", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                signUpProgressBar.setVisibility(View.GONE);
                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        "Something went wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getActivity().getApplicationContext(), "User is already Registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "User Registration Fail!", Toast.LENGTH_SHORT).show();
                            }
                            signUpProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}