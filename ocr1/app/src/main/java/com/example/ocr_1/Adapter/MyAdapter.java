package com.example.ocr_1.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ocr_1.CartList;
import com.example.ocr_1.Model.Cart;
import com.example.ocr_1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<ArrayList<String>> finalData;
    ArrayList<ArrayList<String>> finalDataFull;
    public static LinearLayout linearLayoutItem;
//    ArrayList<Product> productArrayList;

// context-> global interface to our class
    public MyAdapter(Context context, ArrayList<ArrayList<String>> finalData) {
        this.context = context;
//        this.cartArrayList = cartArrayList;
        this.finalData = finalData;
        this.finalDataFull = new ArrayList<>(finalData);

//        this.productArrayList = productArrayList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ArrayList<String> data = finalData.get(position);
        holder.productName.setText(data.get(2));
        holder.description.setText(data.get(3));
        holder.price.setText(data.get(4));
        holder.quantity.setText(data.get(5));

        linearLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,"clicked:  "+finalData.get(position).get(5),Toast.LENGTH_SHORT).show();

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_update_quantity);

                EditText updatedQuantity = dialog.findViewById(R.id.updatedQuantity);
                Button updateCart = dialog.findViewById(R.id.updateCart);

                updatedQuantity.setText(data.get(5));

                updateCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String _updatedQuantity = updatedQuantity.getText().toString().trim();

                        if(TextUtils.isEmpty(_updatedQuantity)) {
                            updatedQuantity.setError("Quantity is required!!");
                            return;
                        } else {
                            String regex = "^[0-9]+$";
                            if (!_updatedQuantity.matches(regex)) {
                                updatedQuantity.setError("Proper Quantity is required!! Contains Only Digits!!!!");
                                return;
                            } else {
                                if (_updatedQuantity.length() > 2) {
                                    updatedQuantity.setError("Quantity should be less than 100 items!!");
                                    return;
                                } else {
                                    if(_updatedQuantity.equals("0")) {
                                        updatedQuantity.setError("Quantity cannot be 0, there must be atleast one Product!!");
                                        return;
                                    }
                                }
                            }
                        }

//                        Toast.makeText(context,_updatedQuantity,Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context,data.toString(),Toast.LENGTH_SHORT).show();

                        Map<String, Object> newData = new HashMap<>();
                        newData.put("quantity",_updatedQuantity);

                        FirebaseDatabase.getInstance().getReference().child("cart").child(finalData.get(position).get(6).toString()).
                                updateChildren(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context,"Quantity updated to "+_updatedQuantity+" Successfully!!",Toast.LENGTH_LONG).show();
                                CartList.myAdapter.notifyItemChanged(position);
                                Intent intent = new Intent(context.getApplicationContext(), CartList.class);
                                context.startActivity(intent);
                            }
                        });

//                        FirebaseDatabase.getInstance().getReference("cart").orderByChild("userId").
//                                equalTo(data.get(0)).addValueEventListener(new ValueEventListener() {
//
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                loop:
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    if(dataSnapshot.getValue(Cart.class).getProductId().toString().equals(data.get(1))) {
//                                        FirebaseDatabase.getInstance().getReference("cart").child(Objects.requireNonNull(dataSnapshot.getKey())).
//                                                updateChildren(newData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
////                                                Toast toast = Toast.makeText(context.getApplicationContext()," Quantity Updated Successfully!!",Toast.LENGTH_SHORT);
////                                                Toast.makeText(context,data.get(2)+" quantity updated successfully!!",Toast.LENGTH_SHORT).show();
//                                                if(task.isSuccessful()) {
////                                                    toast.cancel();
////                                                    toast.show();
//                                                    notifyItemChanged(position);
//                                                    Intent intent = new Intent(context.getApplicationContext(), CartList.class);
//                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    context.startActivity(intent);
//                                                    return;
//                                                }
//                                                else {
//                                                    return;
//                                                }
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(context.getApplicationContext()," Quantity not Updated!!",Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
////                                        Toast.makeText(context,dataSnapshot.getKey(),Toast.LENGTH_SHORT).show();
//                                        break loop;
//                                    }
////                                    Toast.makeText(context,dataSnapshot.getValue().getClass().toString(),Toast.LENGTH_SHORT).show();
//                                }
////                                Toast.makeText(context,data.get(2)+" quantity updated successfully!!",Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Toast.makeText(context,data.get(2)+" quantity not updated!!",Toast.LENGTH_SHORT).show();
//                            }
//                        });

                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        linearLayoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(context,"clicked:  "+finalData.get(position).get(6),Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Delete Product").setMessage("Are you sure, want to delete?")
                        .setIcon(R.drawable.ic_delete).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FirebaseDatabase.getInstance().getReference().child("cart").child(finalData.get(position).get(6).toString()).
                                        removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context,finalData.get(position).get(2).toString()+" product deleted Successfully!!",Toast.LENGTH_LONG).show();
                                        CartList.myAdapter.notifyItemRemoved(position);
                                        Intent intent = new Intent(context.getApplicationContext(), CartList.class);
                                        context.startActivity(intent);
                                    }
                                });

//                                FirebaseDatabase.getInstance().getReference("cart").orderByChild("userId").
//                                        equalTo(data.get(0)).addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                        if(snapshot.getValue(Cart.class).getProductId().toString().equals(data.get(1))) {
//
//                                            FirebaseDatabase.getInstance().getReference("cart").
//                                                    child(Objects.requireNonNull(snapshot.getKey())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                }
//                                            });
//
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                builder.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {

        return finalData.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productName,quantity,price,description;

        public MyViewHolder(View itemView) {

            super(itemView);

            productName = itemView.findViewById(R.id.textProductName);
            description = itemView.findViewById(R.id.textDescription);
            price = itemView.findViewById(R.id.textPrice);
            quantity = itemView.findViewById(R.id.textQuantity);
            linearLayoutItem = itemView.findViewById(R.id.linearLayoutItem);


        }

    }

}
