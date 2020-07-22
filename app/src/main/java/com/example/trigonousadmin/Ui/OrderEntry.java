package com.example.trigonousadmin.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.Model.Admin;
import com.example.trigonousadmin.Model.Customer;
import com.example.trigonousadmin.Model.Order;
import com.example.trigonousadmin.Model.Product;
import com.example.trigonousadmin.R;
import com.example.trigonousadmin.databinding.ActivityOrderEntryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class OrderEntry extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ActivityOrderEntryBinding binding;
    private String adminuid;
    private DatabaseReference databaseReference;
    private android.app.AlertDialog.Builder alretdialog;
    private static final String TAG = "Order_Entry";
    private long lastclicktime = 0;
    String categoriesname;
    ArrayList<String> categorylist;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adminuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        categorylist = new ArrayList<>();
        adapter = new ArrayAdapter<String>(OrderEntry.this,
                android.R.layout.simple_spinner_dropdown_item, categorylist);

        binding.spinnerproductcategory.setAdapter(adapter);
        binding.spinnerproductcategory.setOnItemSelectedListener(this);

        updateprofilefiled(adminuid);
        retrivecategory();

        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        binding.back.setOnClickListener(this);
        binding.orderentry.setOnClickListener(this);

    }

    private void updateprofilefiled(String adminuid) {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Admin").child(adminuid);
        databaseReference2.keepSynced(true);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                binding.adminname.setText(" " + admin.getFullname());
                Glide.with(getApplicationContext()).load(admin.getImageurl())
                        .placeholder(R.drawable.ic_user)
                        .centerCrop()
                        .into(binding.adminPhoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastclicktime < 1000) {
            return;
        }
        lastclicktime = SystemClock.elapsedRealtime();
        if (v == binding.back) {
            finish();
        } else if (v == binding.orderentry) {
            if (filledchecking()) {
                productavailable(categoriesname, binding.ordercodeET.getText().toString());
            } else {
                Toast.makeText(OrderEntry.this, "Please fill the all Information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void productavailable(final String categoriesname, final String ordercode) {
        final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Products")
                .child(categoriesname).child(ordercode);
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product.getProductavailable() >= Integer.parseInt(binding.orderquantityET.getText().toString())) {
                        orderentry();
                    } else {
                        Toast.makeText(OrderEntry.this, "Sorry.available product is= " + product.getProductavailable(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Sorry.Product Stock Out");
                    }
                } else {
                    Toast.makeText(OrderEntry.this, "Sorry.Product code not available", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Sorry.Product code not available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void orderentry() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH) + 1;
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        final String cal = currentYear + "-" + currentMonth + "-" + currentDay;
        String productcode = binding.ordercodeET.getText().toString();
        String productcategory = categoriesname;
        int productquantity = Integer.parseInt(binding.orderquantityET.getText().toString());
        String productcost = binding.ordercostET.getText().toString();
        String deliverycost = binding.deliverycostET.getText().toString();
        int totalcost = Integer.parseInt(binding.ordertotalcostET.getText().toString());
        int totalpaid = Integer.parseInt(binding.totalpaidET.getText().toString());
        int totaldue = Integer.parseInt(binding.totaldueET.getText().toString());
        String customername = binding.customername.getText().toString();
        String customerarea = binding.customerarea.getText().toString();
        String customerphone = binding.customerphoneno.getText().toString();
        String request = binding.orderrequestET.getText().toString();
        String key = databaseReference.push().getKey();

        Order order = new Order(adminuid,cal, productcode, productcategory, productquantity, productcost, deliverycost, totalcost,
                totalpaid, totaldue, customername, customerarea, customerphone, request,false,key);


        databaseReference.child(cal).child(key).setValue(order);

        Toast.makeText(this, "Successfully new Order Added", Toast.LENGTH_SHORT).show();

        updateproductinformation(categoriesname, productcode, productquantity);
        updatecustomerlist(customername, customerarea, customerphone);
        finish();
    }

    private void updatecustomerlist(final String name, final String area, final String phone) {
        final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Customers");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer customer = new Customer(name, area, phone);
                databaseReference3.child(phone).setValue(customer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateproductinformation(String categoriesname, String productcode, final int productquantity) {
        final DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Products")
                .child(categoriesname).child(productcode);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                HashMap<String, Object> hashMap = new HashMap<>();
                int product_sellUD = product.getProductsell() + productquantity;
                int product_availableUD = product.getProductavailable() - productquantity;

                hashMap.put("productsell", product_sellUD);
                hashMap.put("productavailable", product_availableUD);
                databaseReference3.updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private boolean filledchecking() {
        if (binding.ordercodeET.getText().toString().isEmpty() || binding.ordercostET.getText().toString().isEmpty()
                || binding.orderrequestET.getText().toString().isEmpty() || binding.orderquantityET.getText().toString().isEmpty()
                || binding.ordertotalcostET.getText().toString().isEmpty() || binding.totalpaidET.getText().toString().isEmpty()
                || binding.deliverycostET.getText().toString().isEmpty() || binding.totaldueET.getText().toString().isEmpty()
                || binding.customername.getText().toString().isEmpty() || binding.customerarea.getText().toString().isEmpty()
                || binding.customerphoneno.getText().toString().isEmpty() || categoriesname == null) {
            return false;
        } else {
            return true;
        }
    }

    private void retrivecategory() {
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference3.keepSynced(true);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categorylist.clear();
                categorylist.add(0, "Select Category");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "" + snapshot.getKey());
                    categorylist.add(snapshot.getKey().toString());
                }
                Log.d(TAG, "firebase children:" + dataSnapshot.getChildrenCount());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "position:" + position);
        if (position == 0) {
            categoriesname = null;
            binding.spinnerproductcategory.setSelection(0);
        } else {
            categoriesname = categorylist.get(position);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
