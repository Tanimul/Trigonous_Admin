package com.example.trigonousadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trigonousadmin.Model.Admin;
import com.example.trigonousadmin.Ui.AreaEntry;
import com.example.trigonousadmin.Ui.AreaList;
import com.example.trigonousadmin.Ui.CustomerList;
import com.example.trigonousadmin.Ui.OrderEntry;
import com.example.trigonousadmin.Ui.OrderList;
import com.example.trigonousadmin.Ui.ProductEntry;
import com.example.trigonousadmin.Ui.ProductCategories;
import com.example.trigonousadmin.Ui.SellerList;
import com.example.trigonousadmin.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private static final String TAG = "Main_Activity";
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private int image_rec_code = 1;
    private Uri filepath_uri;
    private long backpressed;
    private Toast backtost;
    Toolbar toolbar;
    TextView headerName;
    ImageView headerPic;
    private long lastclicktime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorblack, null));
        binding.drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        View header = binding.homeNavigation.getHeaderView(0);
        headerName = header.findViewById(R.id.header_name);
        headerPic = header.findViewById(R.id.header_photo);

        headerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Choose your profile picture", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        });

        binding.homeNavigation.setNavigationItemSelectedListener(this);

        binding.mainActivity.buttonProductentry.setOnClickListener(this);
        binding.mainActivity.buttonproductlist.setOnClickListener(this);
        binding.mainActivity.buttonOrderentry.setOnClickListener(this);
        binding.mainActivity.buttonCustomerlist.setOnClickListener(this);
        binding.mainActivity.buttonSellerlist.setOnClickListener(this);
        binding.mainActivity.buttonAreaadd.setOnClickListener(this);
        binding.mainActivity.buttonArealist.setOnClickListener(this);
        binding.mainActivity.buttonOrderlist.setOnClickListener(this);

        updatenavheader(firebaseUser.getUid());

    }

    private void updatenavheader(String uid) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(uid);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                headerName.setText(admin.getFullname());
                Glide.with(getApplicationContext()).load(admin.getImageurl())
                        .placeholder(R.drawable.ic_user)
                        .fitCenter()
                        .into(headerPic);
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

        if (v == binding.mainActivity.buttonProductentry) {
            Intent intent = new Intent(MainActivity.this, ProductEntry.class);
            startActivity(intent);
        }
        else if (v == binding.mainActivity.buttonproductlist) {
            Intent intent = new Intent(MainActivity.this, ProductCategories.class);
            startActivity(intent);
        }
        else if (v == binding.mainActivity.buttonOrderentry) {
            Intent intent = new Intent(MainActivity.this, OrderEntry.class);
            startActivity(intent);
        }
        else if (v == binding.mainActivity.buttonCustomerlist) {
            Intent intent = new Intent(MainActivity.this, CustomerList.class);
            startActivity(intent);
        }
        else if (v == binding.mainActivity.buttonSellerlist) {
            Intent intent = new Intent(MainActivity.this, SellerList.class);
            startActivity(intent);
        }
        else if (v == binding.mainActivity.buttonAreaadd) {
            Intent intent = new Intent(MainActivity.this, AreaEntry.class);
            startActivity(intent);
        }
        else if (v == binding.mainActivity.buttonArealist) {
            Intent intent = new Intent(MainActivity.this, AreaList.class);
            startActivity(intent);
        }
        else if (v == binding.mainActivity.buttonOrderlist) {
            Intent intent = new Intent(MainActivity.this, OrderList.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {


            case R.id.nav_about:
                Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_logout:
                firebaseAuth.getInstance().signOut();

                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;


        }
        binding.drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //for Double press for Exit
    @Override
    public void onBackPressed() {

        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(MainActivity.this, "press BACK again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }

        backpressed = System.currentTimeMillis();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_rec_code && resultCode == RESULT_OK && data != null) {
            filepath_uri = data.getData();
            Glide.with(getApplicationContext()).load(filepath_uri).into(headerPic);
            updateadmindetails(firebaseUser.getUid());
        }

    }

    //User Details Update
    public void updateadmindetails(String adminid) {
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("Admin");
        final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Admin").child(adminid);

        if (filepath_uri != null) {
            final ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
            Dialog.setMessage("Updating Profile ...");
            Dialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtention(filepath_uri));

            storageReference2.putFile(filepath_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Dialog.dismiss();

                                    String url = uri.toString();

                                    Toast.makeText(MainActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "done");

                                    databaseReference2.child("imageurl").setValue("" + url);

                                    updatenavheader(firebaseUser.getUid());

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "" + e.getMessage());
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

}
