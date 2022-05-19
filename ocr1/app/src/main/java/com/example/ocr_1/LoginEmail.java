package com.example.ocr_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmail extends AppCompatActivity {

    EditText loginPassword,loginEmail;
    Button login,loginPhoneNumberText;
    TextView registerText,forgetPasswordText;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    String _loginEmail,_loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginVerify);
        registerText = findViewById(R.id.registerText);
        loginPhoneNumberText = findViewById(R.id.loginPhoneNumberText);
        forgetPasswordText = findViewById(R.id.forgetPasswordText);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginEmail.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginEmail.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        loginPhoneNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginEmail.this,LoginPhone.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //all data
                _loginEmail = loginEmail.getText().toString().trim();
                _loginPassword = loginPassword.getText().toString().trim();

                //validate data
                if(TextUtils.isEmpty(_loginEmail)) {
                    loginEmail.setError("Email Id is required!!");
                    return;
                }
                if(TextUtils.isEmpty(_loginPassword)) {
                    loginPassword.setError("Password is required!!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                fAuth.signInWithEmailAndPassword(_loginEmail,_loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginEmail.this, "Logged in Successfully!! Welcome....", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginEmail.this,QrScannerButton.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginEmail.this, "Invalid Credentials!! Try Again....", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}