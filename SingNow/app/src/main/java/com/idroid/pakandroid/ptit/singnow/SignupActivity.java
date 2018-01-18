package com.idroid.pakandroid.ptit.singnow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by admin on 1/18/2018.
 */

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText edtUsername, edtEmail, edtPassword, edtConfirmPW;
    private Button btnSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        mapping();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void mapping() {
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPW = findViewById(R.id.edt_confirmpw);
        btnSignup = findViewById(R.id.btn_signup);
    }

    private void signUp() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmpw = edtConfirmPW.getText().toString();
        if (!password.equals(confirmpw)) {
            Toast.makeText(this, "Mat khau khong khop!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "trung khop", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Dang ky thanh cong!", Toast.LENGTH_SHORT).show();
//                                try {
//                                    Thread.sleep(5000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                Intent swapToLoginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(swapToLoginIntent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
