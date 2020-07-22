package com.example.trigonousadmin.Ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.Model.Admin;
import com.example.trigonousadmin.Model.Product;
import com.example.trigonousadmin.R;
import com.example.trigonousadmin.databinding.ActivityProductEntryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductEntry extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private ActivityProductEntryBinding binding;
    private String adminuid;
    private android.app.AlertDialog.Builder alretdialog;
    private static final String TAG = "Product_Entry";
    private DatabaseReference databaseReference;
    private int image_rec_code = 1;
    private FirebaseAuth firebaseAuth;
    private Uri filepath_uri;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private StorageTask mUploadTask;
    ArrayList<String> categorylist;
    ArrayAdapter<String> adapter;
    String categoriesname, productcode, productcategory;
    boolean edit_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        adminuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        updateprofilefiled(adminuid);
        retrivecategory();

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            binding.toolbarTitle.setText("Product Update");
            binding.productentry.setText("UPDATE");
            productcode = intent.getString("productcode");
            productcategory = intent.getString("productcategory");
            edit_request = intent.getBoolean("edit_request");
            filepath_uri = Uri.parse("not_null");
            updateproductinformation(productcategory, productcode);

        } else {
            binding.productentry.setText("ENTRY");
            binding.toolbarTitle.setText("Product Entry");
        }

        storageReference = FirebaseStorage.getInstance().getReference("Products");
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        categorylist = new ArrayList<>();
        adapter = new ArrayAdapter<String>(ProductEntry.this,
                android.R.layout.simple_spinner_dropdown_item, categorylist);


        progressDialog = new ProgressDialog(this);

        binding.spinnerproductcategory.setAdapter(adapter);
        binding.spinnerproductcategory.setOnItemSelectedListener(this);


        binding.back.setOnClickListener(this);
        binding.productentry.setOnClickListener(this);
        binding.addproductphoto.setOnClickListener(this);
        binding.closeproductimage.setOnClickListener(this);

    }

    private void updateproductinformation(final String productcategory, String productcode) {
        final ProgressDialog Dialog = new ProgressDialog(ProductEntry.this);
        Dialog.setMessage("Please wait ...");
        Dialog.show();
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Products").child(productcategory).child(productcode);
        databaseReference3.keepSynced(true);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Product product = dataSnapshot.getValue(Product.class);

                binding.productnameET.setText(product.getProductname());
                binding.productcodeET.setText(product.getProductcode());
                binding.productquantityET.setText("" + product.getProductquantity());
                binding.productsellET.setText("" + product.getProductsell());
                binding.productavailableET.setText("" + product.getProductavailable());
                binding.productpriceET.setText("" + product.getProductprice());
                binding.productdiscountpriceET.setText("" + product.getProductdiscountprice());
                binding.productdescriptionET.setText(product.getProductdescription());

                binding.spinnerproductcategory.setSelection(adapter.getPosition(productcategory));

                Glide.with(getApplicationContext()).load(product.getProducturl())
                        .placeholder(R.drawable.ic_image).into(binding.imageforproduct);


                binding.addproductphoto.setVisibility(View.GONE);
                binding.productcodeET.setEnabled(false);
                binding.productnameET.setEnabled(false);
                binding.spinnerproductcategory.setEnabled(false);

                Dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //for spinner item
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "position:" + position);
        if (position == 0 || position == categorylist.size() - 1) {
            categoriesname = null;

            if (position != 0) {
                alretdialog = new android.app.AlertDialog.Builder(ProductEntry.this);
                alretdialog.setTitle("Add More Category");
                alretdialog.setMessage("Are you sure to add more Category ?");
                alretdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        alretdialog = new android.app.AlertDialog.Builder(ProductEntry.this);
                        alretdialog.setTitle("Add More Category");
                        alretdialog.setMessage("Are you sure to add more Category ?");
                        // Set an EditText view to get user input
                        final EditText input = new EditText(ProductEntry.this);
                        alretdialog.setView(input);
                        alretdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = input.getText().toString();
                                addcategory(value);
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

            binding.spinnerproductcategory.setSelection(0);
        } else {
            categoriesname = categorylist.get(position);

        }
    }

    private void addcategory(final String value) {
        final DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference4.keepSynced(true);
        databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference4.child(value).setValue(true);
                retrivecategory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //for clicking event
    @Override
    public void onClick(View v) {
        if (v == binding.back) {
            finish();
        } else if (v == binding.productentry) {
            if (edit_request == true) {
                Toast.makeText(this, "Edit Request", Toast.LENGTH_SHORT).show();
                product_edit(productcode, productcategory);

            } else {
                if (filledchecking()) {
                    productcodeavailable(categoriesname, binding.productcodeET.getText().toString());
                } else {
                    Toast.makeText(ProductEntry.this, "Please fill the all Information", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v == binding.addproductphoto) {
            openGallery();
        } else if (v == binding.closeproductimage) {
            binding.imageforproduct.setVisibility(View.GONE);
            binding.closeproductimage.setVisibility(View.GONE);
            binding.addproductphoto.setVisibility(View.VISIBLE);
            filepath_uri = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_rec_code && resultCode == RESULT_OK && data != null) {
            filepath_uri = data.getData();
            binding.closeproductimage.setVisibility(View.VISIBLE);
            binding.imageforproduct.setVisibility(View.VISIBLE);
            binding.addproductphoto.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(filepath_uri).into(binding.imageforproduct);
        }
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
                categorylist.add("Add More Category");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Galary open for place picture
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, image_rec_code);
    }

    //Get image extention
    public String GetFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean filledchecking() {
        if (binding.productcodeET.getText().toString().isEmpty() || binding.productnameET.getText().toString().isEmpty()
                || binding.productpriceET.getText().toString().isEmpty() || binding.productquantityET.getText().toString().isEmpty()
                || binding.productsellET.getText().toString().isEmpty() || binding.productavailableET.getText().toString().isEmpty()
                || binding.productdescriptionET.getText().toString().isEmpty() || binding.productdiscountpriceET.getText().toString().isEmpty()
                || filepath_uri == null || categoriesname == null) {
            return false;
        } else {
            return true;
        }
    }

    private void productcodeavailable(String categoriesname, final String s) {
        databaseReference.child(categoriesname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(s)) {
                    Toast.makeText(ProductEntry.this, "Sorry.you cant can't add the product code", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Sorry.you cant can't add the product code");
                } else {
                    productentry(adminuid);
                    Log.d(TAG, "you can add the product code");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void productentry(final String adminuid) {
        progressDialog.setTitle("Insert the Product Information...");
        progressDialog.show();
        final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));
        storageReference2.putFile(filepath_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String productcode = binding.productcodeET.getText().toString();
                                String productname = binding.productnameET.getText().toString();
                                String productdescription = binding.productdescriptionET.getText().toString();
                                String productcategory = categoriesname;
                                int productprice = Integer.parseInt(binding.productpriceET.getText().toString());
                                int productquantity = Integer.parseInt(binding.productquantityET.getText().toString());
                                int productsell = Integer.parseInt(binding.productsellET.getText().toString());
                                int productavailable = Integer.parseInt(binding.productavailableET.getText().toString());
                                int productdiscountprice = Integer.parseInt(binding.productdiscountpriceET.getText().toString());
                                String url = uri.toString();
                                progressDialog.dismiss();
                                Product product = new Product(adminuid, productcode, productname,
                                        productprice, productquantity, productsell, productavailable, productdiscountprice
                                        , productdescription, productcategory, url);
                                databaseReference.child(productcategory).child(productcode).setValue(product);
                                Toast.makeText(ProductEntry.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "done");
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "" + e.getMessage());
                Toast.makeText(ProductEntry.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void product_edit(final String product_id, final String category) {
        Log.d(TAG, "Product Edit id " + product_id);

        if (filledchecking()) {
            progressDialog.setTitle("Updating the Product Information...");
            progressDialog.show();

            String productdescription = binding.productdescriptionET.getText().toString();
            int productprice = Integer.parseInt(binding.productpriceET.getText().toString());
            int productquantity = Integer.parseInt(binding.productquantityET.getText().toString());
            int productsell = Integer.parseInt(binding.productsellET.getText().toString());
            int productavailable = Integer.parseInt(binding.productavailableET.getText().toString());
            int productdiscountprice = Integer.parseInt(binding.productdiscountpriceET.getText().toString());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("productdescription", productdescription);
            hashMap.put("productprice", productprice);
            hashMap.put("productquantity", productquantity);
            hashMap.put("productsell", productsell);
            hashMap.put("productavailable", productavailable);
            hashMap.put("productdiscountprice", productdiscountprice);

            databaseReference.child(category).child(product_id).updateChildren(hashMap);

            Toast.makeText(ProductEntry.this, "Successfully saved", Toast.LENGTH_SHORT).show();

            finish();
            progressDialog.dismiss();
            Log.d(TAG, "Update Done.");
        } else {
            progressDialog.dismiss();
            Toast.makeText(ProductEntry.this, "Please Put the all Informations ", Toast.LENGTH_SHORT).show();
        }


    }


}
