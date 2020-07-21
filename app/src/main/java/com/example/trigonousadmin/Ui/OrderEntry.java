package com.example.trigonousadmin.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.Model.Admin;
import com.example.trigonousadmin.R;
import com.example.trigonousadmin.databinding.ActivityOrderEntryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderEntry extends AppCompatActivity {
    private ActivityOrderEntryBinding binding;
    private String adminuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adminuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        updateprofilefiled(adminuid);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //  startActivity(new Intent(OrderEntry.this, MainActivity.class));
                finish();
            }
        });

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
}
