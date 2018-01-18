package com.idroid.pakandroid.ptit.singnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by admin on 1/17/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtLinkSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mapping();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        txtLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent swapToSignupIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(swapToSignupIntent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        if (currentUser != null) {
            Toast.makeText(this, "Da dang ky!", Toast.LENGTH_SHORT).show();
            swapToMain();
        } else {
            Toast.makeText(this, "Chua ton tai", Toast.LENGTH_SHORT).show();
        }
    }

    private void mapping() {
        edtEmail = findViewById(R.id.editTextEmail);
        edtPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btn_login);
        txtLinkSignup = findViewById(R.id.txt_linksignup);
    }

    private void logIn() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            swapToMain();
                        } else {
                            Toast.makeText(LoginActivity.this, "Fail.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void swapToMain() {
        Intent swapToMainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(swapToMainIntent);
        finish();
    }
}
