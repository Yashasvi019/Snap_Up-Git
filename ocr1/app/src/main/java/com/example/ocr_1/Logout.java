package com.example.ocr_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Logout extends AppCompatActivity {

    Button logout;
    ProgressBar progressBar;
    ImageView imageArrowBack;
    String loggedUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        logout = findViewById(R.id.logout);
        progressBar = findViewById(R.id.progressBar);
        imageArrowBack = findViewById(R.id.imageArrowBack);

        progressBar.setVisibility(View.GONE);

        loggedUID = FirebaseAuth.getInstance().getUid();

        imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QrScannerButton.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Logout.this).setTitle("Clear Cart").
                        setMessage("Are you sure, this will clear your whole Cart?").setIcon(R.drawable.ic_delete).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        progressBar.setVisibility(View.VISIBLE);
                        FirebaseDatabase.getInstance().getReference().child("cart").orderByChild("userId").
                                equalTo(loggedUID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                                        Toast.makeText(CartList.this,snapshot.toString()+"Inside delete button query!", Toast.LENGTH_SHORT).show();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue();
                                }

                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(Logout.this,LoginEmail.class);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Logout.this, "Error Deleting All Products from your Cart!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

                alertBuilder.show();

            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}