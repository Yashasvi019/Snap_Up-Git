package com.example.ocr_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneLogin extends AppCompatActivity {

    EditText otpNumber1,otpNumber2,otpNumber3,otpNumber4,otpNumber5,otpNumber6;
    TextView phoneNumberText,resendOtpText;
    Button resendOtp,verifyOtp;
    ProgressBar progressBar;
    String loginMobileNumber;
    String _otpNumber1,_otpNumber2,_otpNumber3,_otpNumber4,_otpNumber5,_otpNumber6;
    String enteredOtp,backendOtp;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_login);

        Intent intent = getIntent();
        loginMobileNumber = intent.getStringExtra("loginMobileNumber");

        otpNumber1 = findViewById(R.id.otpNumber1);
        otpNumber2 = findViewById(R.id.otpNumber2);
        otpNumber3 = findViewById(R.id.otpNumber3);
        otpNumber4 = findViewById(R.id.otpNumber4);
        otpNumber5 = findViewById(R.id.otpNumber5);
        otpNumber6 = findViewById(R.id.otpNumber6);
        verifyOtp = findViewById(R.id.verifyOtp);
        resendOtp = findViewById(R.id.resendOtp);
        phoneNumberText = findViewById(R.id.phoneNumberText);
        progressBar = findViewById(R.id.progressBar);
        resendOtpText = findViewById(R.id.resendOtpText);

        phoneNumberText.setText(loginMobileNumber);

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //data
                _otpNumber1 = otpNumber1.getText().toString().trim();
                _otpNumber2 = otpNumber2.getText().toString().trim();
                _otpNumber3 = otpNumber3.getText().toString().trim();
                _otpNumber4 = otpNumber4.getText().toString().trim();
                _otpNumber5 = otpNumber5.getText().toString().trim();
                _otpNumber6 = otpNumber6.getText().toString().trim();

                //validate data
                if(TextUtils.isEmpty(_otpNumber1)) {
                    otpNumber1.setError("Required!!");
                    return;
                }
                if(TextUtils.isEmpty(_otpNumber2)) {
                    otpNumber2.setError("Required!!");
                    return;
                }
                if(TextUtils.isEmpty(_otpNumber3)) {
                    otpNumber3.setError("Required!!");
                    return;
                }
                if(TextUtils.isEmpty(_otpNumber4)) {
                    otpNumber4.setError("Required!!");
                    return;
                }
                if(TextUtils.isEmpty(_otpNumber5)) {
                    otpNumber5.setError("Required!!");
                    return;
                }
                if(TextUtils.isEmpty(_otpNumber6)) {
                    otpNumber6.setError("Required!!");
                    return;
                }

                //otp checking
                if(backendOtp!=null) {

                    enteredOtp = _otpNumber1+_otpNumber2+_otpNumber3+_otpNumber4+_otpNumber5+_otpNumber6;
                    progressBar.setVisibility(View.VISIBLE);
                    phoneAuthCredential = PhoneAuthProvider.getCredential(backendOtp,enteredOtp);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()) {
                                Intent intent = new Intent(getApplicationContext(),QrScannerButton.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(VerifyPhoneLogin.this, "Error: Enter Correct OTP!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(VerifyPhoneLogin.this, "Error: Check Internet Connection!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //otp verification
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                backendOtp = s;
                token = forceResendingToken;
                resendOtpText.setVisibility(View.GONE);
                resendOtp.setVisibility(View.GONE);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendOtpText.setVisibility(View.VISIBLE);
                resendOtp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                resendOtpText.setVisibility(View.GONE);
                resendOtp.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error: Check Internet Connection!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        };

        //sending otp
        sendOtp();

        numberOtpMove();

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendedOtp();
            }
        });

    }

    public void sendOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(loginMobileNumber,60, TimeUnit.SECONDS,this,callbacks);
    }

    public void resendedOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(loginMobileNumber,60, TimeUnit.SECONDS,this,callbacks,token);
    }


    private void numberOtpMove() {

        otpNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    otpNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otpNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    otpNumber3.requestFocus();
                } else {
                    otpNumber1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otpNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    otpNumber4.requestFocus();
                } else {
                    otpNumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otpNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    otpNumber5.requestFocus();
                } else {
                    otpNumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otpNumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()) {
                    otpNumber6.requestFocus();
                } else {
                    otpNumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        otpNumber6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().isEmpty()) {
                    otpNumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

    }

    @Override
    public void onBackPressed() {

    }

}