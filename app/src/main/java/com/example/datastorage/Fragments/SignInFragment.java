package com.example.datastorage.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.datastorage.Activities.DashboardActivity;
import com.example.datastorage.Models.UserModel;
import com.example.datastorage.R;
import com.example.datastorage.Utils.Helper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    TextInputEditText signInEmail, signInPassword;
    ProgressBar signInProgressBar;
    Button signInButton;
    TextView registerButton;
    private final String USER_ID = "userId";
    private final String USER_NAME = "userName";
    // Firebase Connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        signInEmail = view.findViewById(R.id.signInEmail);
        signInPassword = view.findViewById(R.id.signInPassword);
        signInButton = view.findViewById(R.id.signInButton);
        registerButton = view.findViewById(R.id.registerButton);
        signInProgressBar = view.findViewById(R.id.signInProgressbar);

        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerButton) {
            Navigation.findNavController(view)
                    .navigate(SignInFragmentDirections.signInFragmentToSignUpFragment());
        } else if (view.getId() == R.id.signInButton) {
            if (Helper.isNetworkAvailable(getContext())) {
                userLogin();
            }

        }
    }

    private void userLogin() {
        String email = signInEmail.getText().toString();
        String password = signInPassword.getText().toString();

        if (TextUtils.isEmpty(signInEmail.getText().toString()) ||
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signInEmail.setError("Enter a valid email address", null);
            signInEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(signInPassword.getText().toString())) {
            signInPassword.setError("Enter password", null);
            signInPassword.requestFocus();
            return;
        }

        signInProgressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        signInProgressBar.setVisibility(View.GONE);
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {

                            String currentUserId = currentUser.getUid();

                            collectionReference
                                    .whereEqualTo(USER_ID, currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value != null) {
                                                for (QueryDocumentSnapshot snapshot : value) {
                                                    // Global Journal User
                                                    UserModel userModel = UserModel.getInstance();
                                                    userModel.setUserId(snapshot.getString(USER_ID));
                                                    userModel.setUserName(snapshot.getString(USER_NAME));

                                                    getActivity().finish();
                                                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If Failed:
                        Toast.makeText(getActivity(),
                                "Something went wrong " + e, Toast.LENGTH_LONG).show();
                    }
                });

    }
}