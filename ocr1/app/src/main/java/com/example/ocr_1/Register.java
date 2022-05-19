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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocr_1.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText firstName,lastName,email,phoneNumber,age,password,confirmpassword,countryCode;
    Button register;
    TextView loginText;
    RadioGroup genderCheck;
    ProgressBar progressBar;
    RadioButton gender;
    TextView genderText;
    String _firstName,_lastName,_email,_phoneNumber,_age,_gender,_password,_confirmpassword,_countryCode;
    FirebaseAuth fAuth;
    FirebaseDatabase fDatabase;
    DatabaseReference rDatabase;
    static final String USER = "user";
    static final String TAG = "RegisterActivity";
    User user;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        countryCode = findViewById(R.id.countryCode);
        phoneNumber = findViewById(R.id.phoneNumber);
        age = findViewById(R.id.age);
        genderCheck = (RadioGroup)findViewById(R.id.gender);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmPassword);
        register = findViewById(R.id.register);
        loginText = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progressBar);
        genderText = findViewById(R.id.genderText);

        fAuth = FirebaseAuth.getInstance();

        fDatabase = FirebaseDatabase.getInstance();
        rDatabase = fDatabase.getReference(USER);

        //getting current user
        if(fAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Register.this,LoginEmail.class);
            startActivity(intent);
            finish();
        }

        // Uncheck or reset the radio buttons initially
        genderCheck.clearCheck();

        // Add the Listener to the RadioGroup
        genderCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    // The flow will come here when any of the radio buttons in the radioGroup has been clicked. Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {
                        // Get the selected Radio Button
                        gender = (RadioButton)group.findViewById(checkedId);
                        _gender = gender.getText().toString().trim();
//                        Toast.makeText(Register.this,"Clicked="+gender.getText().toString() , Toast.LENGTH_SHORT).show();
                    }
                });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, LoginEmail.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //all data
                _firstName = firstName.getText().toString().trim();
                _lastName = lastName.getText().toString().trim();
                _email = email.getText().toString().trim();
                _countryCode = countryCode.getText().toString().trim();
                _phoneNumber = phoneNumber.getText().toString().trim();
                _age = age.getText().toString().trim();
                _password = password.getText().toString().trim();
                _confirmpassword = confirmpassword.getText().toString().trim();

                //validate data
                if(TextUtils.isEmpty(_firstName)) {
                    firstName.setError("First Name is required!!");
                    return;
                } else {
                    String regex = "[a-zA-Z]+";
                    if(!_firstName.matches(regex)){
                        firstName.setError("Proper First Name is required!! Contains Only Alphabets!!!!");
                        return;
                    }
                }
                if(TextUtils.isEmpty(_lastName)) {
                    lastName.setError("Last Name is required!!");
                    return;
                } else {
                    String regex = "[a-zA-Z]+";
                    if(!_lastName.matches(regex)){
                        lastName.setError("Proper Last Name is required!! Contains Only Alphabets!!!!");
                        return;
                    }
                }
                if(TextUtils.isEmpty(_email)) {
                    email.setError("Email Id is required!!");
                    return;
                } else {
                    String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,3}+$";
//                    "[a-zA-Z0-9]+@[a-z]+\\.[a-z]{2,3}";
                    if(!_email.matches(regex)){
                        email.setError("Proper Email Id is required!!");
                        return;
                    }
                }
                if(TextUtils.isEmpty(_countryCode)) {
                    countryCode.setError("Country Code is required!!");
                    return;
                } else {
                    if(_countryCode.length()>3) {
                        countryCode.setError("Country Code Contains till 3 Digits Only!!!!");
                        return;
                    }
                }
                if(TextUtils.isEmpty(_phoneNumber)) {
                    phoneNumber.setError("Phone Number is required!!");
                    return;
                } else {
                    String regex = "^[0-9]+$";
//                    \d{10}|(?:\d{3}-){2}\d{4}|\(\d{3}\)\d{3}-?\d{4}
                    if(!_phoneNumber.matches(regex)){
                        phoneNumber.setError("Proper Phone Number is required!! Contains Only Digits!!!!");
                        return;
                    } else {
                        if(_phoneNumber.length()!=10) {
                            phoneNumber.setError("Phone Number Contains 10 Digits Only!!!!");
                            return;
                        }
                    }
                }
                if(TextUtils.isEmpty(_age)) {
                    age.setError("Age is required!!");
                    return;
                } else {
                    String regex = "^[0-9]+$";
                    if(!_age.matches(regex)){
                        age.setError("Proper Age is required!! Contains Only Digits!!!!");
                        return;
                    } else {
                        if(_age.length()>3) {
                            age.setError("Age Contains Upto 3 Digits Only!!!!");
                            return;
                        }
                    }
                }
                if(TextUtils.isEmpty(_gender)) {
                    Toast.makeText(Register.this, "Gender is required!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(_password)) {
                    password.setError("Password is required!!");
                    return;
                } else {
                    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
                    if(!_password.matches(regex)){
                        password.setError("Proper Password is required!! Length must be greater than 6, " +
                                "Password Must Contains a digit, a lower case letter, a upper case letter, a special character atleast once!!!! " +
                                "No whitespace are allowed in the entire string");
                        return;
                    }
                }
                if(TextUtils.isEmpty(_confirmpassword)) {
                    confirmpassword.setError("Confirm Password is required!!");
                    return;
                } else {
                    if(!_password.equals(_confirmpassword)) {
                        confirmpassword.setError("Password and Confirm Password doesn't match!!");
                        return;
                    }
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase
                user = new User(_firstName,_lastName,_email,_phoneNumber,_age,_gender,_password);
                registerUser(_email,_password);

            }
        });

    }

    public void registerUser(String _email, String _password) {
        fAuth.createUserWithEmailAndPassword(_email,_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //sign-in success, update UI with the signed-in user's information
                    Log.d(TAG, "CreateUserWithEmailAndPassword:Success");
                    Toast.makeText(Register.this, "Registered Successfully!! Now Link your account, to login through your Phone Number also...", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = fAuth.getCurrentUser();
                    updateUI(user);
                    mobileNumber=_countryCode+_phoneNumber;
                    Intent intent = new Intent(Register.this,VerifyPhoneRegister.class);
                    intent.putExtra("mobileNumber",mobileNumber);
                    startActivity(intent);
                    Log.d(TAG,"OnSuccess: "+mobileNumber, task.getException());
                } else {
                    //if sign-in fails, display a message to the user
                    Log.d(TAG,"CreateUserWithEmailAndPassword:Failure  ", task.getException());
//                    Toast.makeText(Register.this, "Not Registered Yet!! Authentication Failed.....", Toast.LENGTH_SHORT).show();
                    Toast.makeText(Register.this, "User already Registered!! Authentication Failed.....", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void updateUI(FirebaseUser currentUser) {
        String KeyId = rDatabase.push().getKey();
        rDatabase.child(KeyId).setValue(user);
    }

    @Override
    public void onBackPressed() {

    }

}