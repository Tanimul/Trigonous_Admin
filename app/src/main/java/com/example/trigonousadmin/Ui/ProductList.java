package com.example.trigonousadmin.Ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.trigonousadmin.Adapter.CategoryAdapter;
import com.example.trigonousadmin.Adapter.ProductListAdapter;
import com.example.trigonousadmin.Interface.ItemClickInterface;
import com.example.trigonousadmin.Model.Product;

import com.example.trigonousadmin.databinding.ActivityProductListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductList extends AppCompatActivity implements ItemClickInterface {
    private ActivityProductListBinding binding;
    private String productcategory;
    private ArrayList<Product> products = new ArrayList<Product>();
    private static final String TAG = "Product_List";
    private ProductListAdapter productListAdapter;
    private android.app.AlertDialog.Builder alretdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            productcategory = intent.getString("productcategory");
            binding.toolbarTitle.setText(productcategory);
            readproduct(productcategory);
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.productSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchproduct(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.recyclerViewProductlist.setFitsSystemWindows(true);
        binding.recyclerViewProductlist.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewProductlist.setHasFixedSize(true);
        productListAdapter = new ProductListAdapter(products, this, this);
        binding.recyclerViewProductlist.setAdapter(productListAdapter);


    }

    private void readproduct(String productcategory) {
        final ProgressDialog Dialog = new ProgressDialog(ProductList.this);
        Dialog.setMessage("Please wait ...");
        Dialog.show();
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Products").child(productcategory);
        databaseReference3.keepSynced(true);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    products.add(product);

                }
                Dialog.dismiss();
                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void searchproduct(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Products").child(productcategory).orderByKey()
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Product product = snapshot.getValue(Product.class);
                    products.add(product);

                }
                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void OnItemsingleClick(String itemname) {
        Intent intent = new Intent(ProductList.this, ProductDetails.class);
        intent.putExtra("productcode", itemname);
        intent.putExtra("productcategory", productcategory);
        startActivity(intent);
    }

    @Override
    public void OnItemLongClick(final String itemname) {

        alretdialog = new android.app.AlertDialog.Builder(ProductList.this);
        alretdialog.setTitle("Update the Product Information");
        alretdialog.setMessage("Are tou sure to Update the Product Information?");
        alretdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ProductList.this, ProductEntry.class);
                intent.putExtra("productcode", itemname);
                intent.putExtra("productcategory", productcategory);
                intent.putExtra("edit_request", true);
                startActivity(intent);
//                finish();
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
