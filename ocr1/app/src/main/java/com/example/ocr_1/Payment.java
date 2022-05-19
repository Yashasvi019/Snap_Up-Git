package com.example.ocr_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Payment extends AppCompatActivity implements PaymentResultListener {

    TextView textAmount,textpayId1,textpayId2,textPayStatus,payId;
    Button razorpayBtn,continuePayBtn,logoutBtn,continueShoppingBtn;
    String amount,loggedUID;
    Double finalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout.preload(getApplicationContext());

        textAmount = findViewById(R.id.textAmount);
        textPayStatus = findViewById(R.id.textPayStatus);
        payId = findViewById(R.id.payId);
        textpayId1 = findViewById(R.id.textpayId1);
        textpayId2 = findViewById(R.id.textPayId2);
        razorpayBtn = findViewById(R.id.razorpayBtn);
        continuePayBtn = findViewById(R.id.continuePayBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        continueShoppingBtn = findViewById(R.id.continueShoppingBtn);

        loggedUID = FirebaseAuth.getInstance().getUid();

        Intent intent = getIntent();
        amount = intent.getStringExtra("totalAmount");

        textAmount.setText("Rs. "+amount);
        textPayStatus.setText("Payment Not Done Yet!");
        payId.setVisibility(View.GONE);
        continuePayBtn.setVisibility(View.GONE);
        logoutBtn.setVisibility(View.GONE);

        razorpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"clicked razorpay button",Toast.LENGTH_SHORT).show();
                makePayment();
            }

        });

        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"clicked continue shopping button",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Payment.this,CartList.class);
                startActivity(intent);
                finish();
            }
        });

        continuePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"clicked continue pay button",Toast.LENGTH_SHORT).show();
                makePayment();
            }

        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"clicked logout button",Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Payment.this).setTitle("Clear Cart").
                        setMessage("Are you sure, this will clear your whole Cart?").setIcon(R.drawable.ic_delete).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FirebaseDatabase.getInstance().getReference().child("cart").orderByChild("userId").
                                        equalTo(loggedUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                        Toast.makeText(CartList.this,snapshot.toString()+"Inside delete button query!", Toast.LENGTH_SHORT).show();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            dataSnapshot.getRef().removeValue();
                                        }

                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(Payment.this,LoginEmail.class);
                                        startActivity(intent);
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                        Toast.makeText(Payment.this, "Error Deleting All Products from your Cart!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                alertBuilder.show();


            }

        });

    }

    public void makePayment() {

        finalAmount = Double.parseDouble(amount)*100;
        finalAmount= Double.valueOf(String.format("%.0f",finalAmount));

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_RTRz6WBhG6zV25");

        checkout.setImage(R.drawable.logo);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Snap Up");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", finalAmount);//pass amount in currency subunits
            options.put("prefill.email", "agarwalyashu019@gmail.com");
            options.put("prefill.contact","7374918422");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        payId.setVisibility(View.VISIBLE);
        textPayStatus.setText(":) Payment Done Successfully!");
        textpayId1.setText("Transaction/Payment Id:   ");
        textpayId2.setText(s);
        logoutBtn.setVisibility(View.VISIBLE);
        razorpayBtn.setVisibility(View.GONE);
        continuePayBtn.setVisibility(View.GONE);
        continueShoppingBtn.setVisibility(View.GONE);
    }

    @Override
    public void onPaymentError(int i, String s) {
        payId.setVisibility(View.VISIBLE);
        textPayStatus.setText(":( Payment Failed!");
        textpayId1.setText("Issue:   ");
//        String x = s+finalAmount;
        textpayId2.setText(s);
        continuePayBtn.setVisibility(View.VISIBLE);
        logoutBtn.setVisibility(View.VISIBLE);
        razorpayBtn.setVisibility(View.GONE);
        continueShoppingBtn.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

    }

}