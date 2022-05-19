package com.example.ocr_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ForgetPassword extends AppCompatActivity {

    EditText registeredEmail;
    Button linkSendBtn,backLoginBtn;
    ProgressBar progressBar;
    String _email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        registeredEmail = findViewById(R.id.registeredEmail);
        linkSendBtn = findViewById(R.id.linkSendBtn);
        backLoginBtn = findViewById(R.id.backLoginBtn);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        linkSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //all data
                _email = registeredEmail.getText().toString().trim();

                //validate data
                if(TextUtils.isEmpty(_email)) {
                    registeredEmail.setError("Email Id is required!!");
                    return;
                } else {
                    String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,3}+$";
//                    "[a-zA-Z0-9]+@[a-z]+\\.[a-z]{2,3}";
                    if(!_email.matches(regex)){
                        registeredEmail.setError("Proper Email Id is required!!");
                        return;
                    }
                }

                //query
                firebaseAuth.sendPasswordResetEmail(_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            Toast.makeText(ForgetPassword.this, "Link Sent Successfully.....", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ForgetPassword.this, "Check you e-mail id please.....", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgetPassword.this,LoginEmail.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ForgetPassword.this, "Link not Sent.....", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ForgetPassword.this, "User not Registered!!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ForgetPassword.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ForgetPassword.this, "Link not Sent.....", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ForgetPassword.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        });

        backLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPassword.this,LoginEmail.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}