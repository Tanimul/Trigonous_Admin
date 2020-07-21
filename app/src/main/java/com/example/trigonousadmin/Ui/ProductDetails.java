package com.example.trigonousadmin.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.trigonousadmin.Adapter.ProductListAdapter;
import com.example.trigonousadmin.Model.Admin;
import com.example.trigonousadmin.Model.Product;
import com.example.trigonousadmin.R;
import com.example.trigonousadmin.databinding.ActivityProductDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDetails extends AppCompatActivity {
    private ActivityProductDetailsBinding binding;
    private String productcode, productcategory;
    private static final String TAG = "Product_Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            productcode = intent.getString("productcode");
            productcategory = intent.getString("productcategory");
            readproduct(productcode, productcategory);
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void readproduct(String productcode, String productcategory) {
        final ProgressDialog Dialog = new ProgressDialog(ProductDetails.this);
        Dialog.setMessage("Please wait ...");
        Dialog.show();
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Products").child(productcategory).child(productcode);
        databaseReference3.keepSynced(true);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Product product = dataSnapshot.getValue(Product.class);

                binding.productname.setText(product.getProductname());
                binding.productCode.setText(product.getProductcode());
                binding.productquantityTV.setText(binding.productquantityTV.getText() + "" + product.getProductquantity());
                binding.productsellTV.setText(binding.productsellTV.getText() + "" + product.getProductsell());
                binding.productavailableTV.setText(binding.productavailableTV.getText() + "" + product.getProductavailable());
                binding.productpriceTV.setText(binding.productpriceTV.getText() + "" + product.getProductprice());
                binding.productdiscountpriceTV.setText(binding.productdiscountpriceTV.getText() + "" + product.getProductdiscountprice());
                binding.productdescriptionET.setText(binding.productdescriptionET.getText() + "" + product.getProductdescription());

                Glide.with(getApplicationContext()).load(product.getProducturl())
                        .placeholder(R.drawable.ic_image).into(binding.productPhoto);

                Dialog.dismiss();

                publishername(product.getAdminid());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void publishername(String adminid) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(adminid);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                binding.publishbyadmin.setText(binding.publishbyadmin.getText() + " " + admin.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
