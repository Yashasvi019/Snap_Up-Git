package com.example.ocr_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocr_1.Adapter.MyAdapter;
import com.example.ocr_1.Model.Cart;
import com.example.ocr_1.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CartList extends AppCompatActivity {

    private static final String TAG = "CartActivity";

    RecyclerView recyclerView;
    DatabaseReference databaseReferenceProduct;
    DatabaseReference databaseReference;
    public static MyAdapter myAdapter;
    ArrayList<Cart> cartArrayList;
    ArrayList<Product> productArrayList = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<ArrayList<String>> finalData = new ArrayList<ArrayList<String>>();
    TextView textTotalAmount;
    Double totalAmount=0.0;
    ArrayList<String> cartKeys = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout02;
    TextView textCheckCart;
    ImageView imageArrowBack,imageDeleteFull;
    Button continueBtn;
    EditText searchOption;

    String loggedUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        recyclerView = findViewById(R.id.recylerView);
        textTotalAmount = findViewById(R.id.textTotalAmount);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        linearLayout02 = findViewById(R.id.linearLayout02);
        textCheckCart = findViewById(R.id.textCheckCart);
        imageArrowBack = findViewById(R.id.imageArrowBack);
        imageDeleteFull = findViewById(R.id.imageDeleteFull);
        continueBtn = findViewById(R.id.continueBtn);
        searchOption = findViewById(R.id.searchOption);

//        textTotalAmount.setText(totalAmount+"  Rs. ");

        loggedUID = FirebaseAuth.getInstance().getUid();

        databaseReferenceProduct = FirebaseDatabase.getInstance().getReference().child("product");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartArrayList = new ArrayList<>();

        myAdapter = new MyAdapter(this, finalData);
        recyclerView.setAdapter(myAdapter);

        imageArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QrScannerButton.class);
                startActivity(intent);
            }
        });

        imageDeleteFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(CartList.this,"Clicked:  Delete All Image!", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CartList.this).setTitle("Clear Cart").setMessage("Are you sure, want to clear your Cart?")
                        .setIcon(R.drawable.ic_delete).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                                        Toast.makeText(CartList.this, "Successfully Deleted All Products from your Cart!", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                        Toast.makeText(CartList.this, "Error Deleting All Products from your Cart!", Toast.LENGTH_SHORT).show();
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

        databaseReferenceProduct.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    for(DataSnapshot dataSnapshot: task.getResult().getChildren()) {
                        String key =dataSnapshot.getKey();
                        Product product = dataSnapshot.getValue(Product.class);
                        productArrayList.add(product);
                    }
                }
            }
        });

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    for(DataSnapshot dataSnapshot: task.getResult().getChildren()) {
                        String key = dataSnapshot.getKey().toString();
                        Cart cart = dataSnapshot.getValue(Cart.class);
                        if (loggedUID.equals(cart.getUserId())) {
                            cartArrayList.add(cart);
                            cartKeys.add(key);
//                            cartArrayList.add(key);
    //                        System.out.println(cart.getUserId());
    //                        Log.d("USER", cart.getUserId());
                        }

                    }

                    if(cartArrayList.isEmpty()) {
                        linearLayout02.setVisibility(View.VISIBLE);
                        textCheckCart.setVisibility(View.VISIBLE);
                        textCheckCart.setText("NOTHING TO DISPLAY! \nNO ITEM ADDED YET!!");
                    }

                    int i=0;
                    outerloop:
                    for (Cart dataCart : cartArrayList) {
                        data.clear();
                        innerloop:
                        for (Product dataProduct : productArrayList) {
                            if (dataCart.getProductId().equals(dataProduct.getpId())) {
                                data.add(dataCart.getUserId());
                                data.add(dataCart.getProductId());
                                data.add(dataProduct.getProductName());
                                data.add(dataProduct.getDescription());
                                data.add(dataProduct.getPrice());
                                data.add(dataCart.getQuantity());
                                data.add(cartKeys.get(i));
//                                totalAmount=totalAmount+(Integer.parseInt(dataProduct.getPrice().toString())*Integer.parseInt((dataCart.getQuantity().toString())));
                                finalData.add(new ArrayList<String>(data));
                                i++;
                                break innerloop;
                            }
                        }
                    }
                    String price,quantity;
                    totalAmount=0.0;
                    for (ArrayList<String> data: finalData) {
                        price=data.get(4).toString();
                        quantity=data.get(5).toString();
                        totalAmount=totalAmount+(Double.parseDouble(price)*Double.parseDouble(quantity));
                    }
                    totalAmount= Double.valueOf(String.format("%.3f",totalAmount));
                    textTotalAmount.setText(totalAmount+"  Rs. ");
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                Intent intent = new Intent(CartList.this, CartList.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
//                Collections.shuffle(finalData, new Random(System.currentTimeMillis()));
//                myAdapter = new MyAdapter(CartList.this,finalData);
//                recyclerView.setAdapter(myAdapter);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"clicked ",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartList.this,Payment.class);
                String amount = Double.toString(totalAmount);
                intent.putExtra("totalAmount",amount);
                startActivity(intent);
            }
        });

        searchOption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(CartList.this,charSequence,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Toast.makeText(CartList.this,editable,Toast.LENGTH_SHORT).show();
                search(editable.toString());
            }
        });

    }

    public void search(String s) {

        ArrayList<ArrayList<String>> searchData = new ArrayList<ArrayList<String>>();

        for(ArrayList<String> data:finalData) {
            if(data.get(2).toString().toLowerCase().contains(s.toLowerCase())) {
                searchData.add(new ArrayList<String>(data));
            }
        }

        myAdapter = new MyAdapter(this,searchData);
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed() {

    }

}