package com.example.trigonousadmin.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trigonousadmin.Adapter.CategoryAdapter;
import com.example.trigonousadmin.Interface.ItemClickInterface;
import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.databinding.ActivityProductCategoriesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductCategories extends AppCompatActivity implements ItemClickInterface {
    private ActivityProductCategoriesBinding binding;
    private static final String TAG = "Product_Categories";
    private android.app.AlertDialog.Builder alretdialog;

    CategoryAdapter categoryAdapter;
    ArrayList<String> categorylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.recyclerViewCategory.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        binding.recyclerViewCategory.setLayoutManager(linearLayoutManager);
        categorylist = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categorylist, this);
        binding.recyclerViewCategory.setAdapter(categoryAdapter);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retrivecategory();

    }

    private void retrivecategory() {
        final ProgressDialog Dialog = new ProgressDialog(ProductCategories.this);
        Dialog.setMessage("Please wait ...");
        Dialog.show();
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference3.keepSynced(true);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categorylist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "" + snapshot.getKey());
                    categorylist.add(snapshot.getKey().toString());
                    Dialog.dismiss();
                }
                Log.d(TAG, "firebase children:" + dataSnapshot.getChildrenCount());
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void OnItemsingleClick(String itemname) {
        Intent intent = new Intent(ProductCategories.this, ProductList.class);
        intent.putExtra("productcategory", itemname);
        startActivity(intent);
    }

    @Override
    public void OnItemLongClick(final String itemname) {
        alretdialog = new android.app.AlertDialog.Builder(ProductCategories.this);
        alretdialog.setTitle("Update the Product Category");
        alretdialog.setMessage("Are tou sure to Update the Product Category?");
        alretdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                alretdialog = new android.app.AlertDialog.Builder(ProductCategories.this);
                alretdialog.setTitle("Update Category Name");
                alretdialog.setMessage("Are you sure to Update the Category Name?");
                // Set an EditText view to get user input
                final EditText input = new EditText(ProductCategories.this);
                alretdialog.setView(input);
                alretdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        updatecategory(itemname,value);
                    }
                });

                alretdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alretdialog.create();
                alretdialog.show();
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

    private void updatecategory(final String itemname, final String value) {
        final DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference4.keepSynced(true);
        databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    databaseReference4.child(itemname).setValue(null);
                    databaseReference4.child(value).setValue(true);
                    retrivecategory();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}

