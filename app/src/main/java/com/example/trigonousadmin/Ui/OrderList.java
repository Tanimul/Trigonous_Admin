package com.example.trigonousadmin.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.trigonousadmin.Adapter.OrderListAdapter;
import com.example.trigonousadmin.Adapter.ProductListAdapter;
import com.example.trigonousadmin.Interface.ItemClickInterface;
import com.example.trigonousadmin.Interface.OrderItemClikInterface;
import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.Model.Order;
import com.example.trigonousadmin.Model.Product;
import com.example.trigonousadmin.databinding.ActivityOrderListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class OrderList extends AppCompatActivity implements OrderItemClikInterface {
    private ActivityOrderListBinding binding;
    private ArrayList<Order> orders;
    private static final String TAG = "Order_List";
    private OrderListAdapter orderListAdapter;
    private android.app.AlertDialog.Builder alretdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orders = new ArrayList<Order>();
        binding.recyclerViewOrderlist.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewOrderlist.setHasFixedSize(true);
        orderListAdapter = new OrderListAdapter(orders, this, this);
        binding.recyclerViewOrderlist.setAdapter(orderListAdapter);
        readorder();

    }


    @Override
    public void OnorderLongClick(final String date, final String key, boolean completedelivery) {
        if(!completedelivery){
            alretdialog = new android.app.AlertDialog.Builder(OrderList.this);
            alretdialog.setTitle("Delivery Complete");
            alretdialog.setMessage("Are you sure to Delivery is completed?");
            alretdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Orders")
                            .child(date).child(key);
                    databaseReference3.keepSynced(true);
                    databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("completedelivery", true);
                            databaseReference3.updateChildren(hashMap);
                            Toast.makeText(OrderList.this, "Delivery Complete", Toast.LENGTH_SHORT).show();
                            readorder();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            alretdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            alretdialog.create();
            alretdialog.show();
        }

    }

    private void readorder() {
        final ProgressDialog Dialog = new ProgressDialog(OrderList.this);
        Dialog.setMessage("Please wait ...");
        Dialog.show();
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Orders");
        databaseReference3.keepSynced(true);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                        Dialog.dismiss();
                        Order order = snapshot.getValue(Order.class);
                        orders.add(order);
                        Log.d(TAG, " Product Code: " + order.getProductcode());
                    }
                }
                Collections.reverse(orders);


                orderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
