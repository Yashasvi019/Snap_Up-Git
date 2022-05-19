package com.example.ocr_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhone extends AppCompatActivity {

    EditText loginCountryCode,loginPhoneNumber;
    Button loginVerify;
    Button loginEmailText;
    ProgressBar progressBar;
    String _loginCountryCode,_loginPhoneNumber;
    String loginMobileNumber;
    static final String TAG = "LoginPhoneActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        loginCountryCode = findViewById(R.id.loginCountryCode);
        loginPhoneNumber = findViewById(R.id.loginPhoneNumber);
        loginVerify = findViewById(R.id.loginVerify);
        loginEmailText = findViewById(R.id.loginEmailText);
        progressBar = findViewById(R.id.progressBar);

        loginEmailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPhone.this,LoginEmail.class);
                startActivity(intent);
            }
        });

        loginVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //data
                _loginCountryCode = loginCountryCode.getText().toString().trim();
                _loginPhoneNumber = loginPhoneNumber.getText().toString().trim();

                //validate data
                if(TextUtils.isEmpty(_loginCountryCode)) {
                    loginCountryCode.setError("Country Code is required!!");
                    return;
                } else {
                    if(_loginCountryCode.length()>3) {
                        loginCountryCode.setError("Country Code Contains till 3 Digits Only!!!!");
                        return;
                    }
                }
                if(TextUtils.isEmpty(_loginPhoneNumber)) {
                    loginPhoneNumber.setError("Phone Number is required!!");
                    return;
                } else {
                    String regex = "^[0-9]+$";
                    if(!_loginPhoneNumber.matches(regex)){
                        loginPhoneNumber.setError("Proper Phone Number is required!! Contains Only Digits!!!!");
                        return;
                    } else {
                        if(_loginPhoneNumber.length()!=10) {
                            loginPhoneNumber.setError("Phone Number Contains 10 Digits Only!!!!");
                            return;
                        }
                    }
                }

                loginMobileNumber=_loginCountryCode+_loginPhoneNumber;

                progressBar.setVisibility(View.VISIBLE);

                Intent intent = new Intent(LoginPhone.this,VerifyPhoneLogin.class);
                intent.putExtra("loginMobileNumber",loginMobileNumber);
                startActivity(intent);
                Log.d(TAG,"OnSuccess: "+loginMobileNumber);

            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}
