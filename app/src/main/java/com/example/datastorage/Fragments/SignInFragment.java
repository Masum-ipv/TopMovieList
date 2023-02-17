package com.example.datastorage.Fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.datastorage.Activities.DashboardActivity;
import com.example.datastorage.R;
import com.example.datastorage.Utils.Helper;
import com.example.datastorage.Utils.SharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    TextInputEditText signInEmail, signInPassword;
    ProgressBar signInProgressBar;
    Button signInButton;
    TextView registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
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
            getParentFragmentManager().beginTransaction().
                    replace(R.id.fragmentContainer, new SignUpFragment()).
                    commit();
        } else if (view.getId() == R.id.signInButton) {
            if (Helper.isNetworkAvailable(getContext())) {
                userLogin();
            }

        }
    }

    private void userLogin() {
        String email = signInEmail.getText().toString();
        String password = signInPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signInEmail.setError("Enter a valid email address", null);
            signInEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            signInPassword.setError("Enter password", null);
            signInPassword.requestFocus();
            return;
        }

        signInProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signInProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}