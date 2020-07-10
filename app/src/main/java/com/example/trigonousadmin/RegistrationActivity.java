package com.example.trigonousadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.trigonousadmin.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });


        binding.buttonRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String admin_name = binding.edittextUsername.getText().toString();
                String full_name = binding.edittextFullname.getText().toString();
                String email = binding.edittextEmailForRegistration.getText().toString();
                String password = binding.edittextPasswordForRegistration.getText().toString();

                if (TextUtils.isEmpty(admin_name)) {
                    binding.edittextUsername.setError("Required filed");
                    binding.edittextUsername.requestFocus();
                }
                ;
                if (TextUtils.isEmpty(full_name)) {
                    binding.edittextFullname.setError("Required filed");
                    binding.edittextFullname.requestFocus();
                }

                if (TextUtils.isEmpty(email)) {
                    binding.edittextEmailForRegistration.setError("Required filed");
                    binding.edittextEmailForRegistration.requestFocus();
                }
                ;
                if (TextUtils.isEmpty(password)) {
                    binding.edittextPasswordForRegistration.setError("Required filed");
                    binding.edittextPasswordForRegistration.requestFocus();
                }
                if (password.length() < 6) {
                    binding.edittextPasswordForRegistration.setError("Password must have 6 character");
                    binding.edittextPasswordForRegistration.requestFocus();
                } else {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                    register(admin_name, full_name, email, password);
                }

            }
        });
    }

    private void register(final String adminname, final String fullname, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    String adminid = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Admin").child(adminid);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", adminid);
                    hashMap.put("adminname", adminname.toLowerCase());
                    hashMap.put("fullname", fullname);
                    hashMap.put("imageurl", null);

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "You can't Register with this email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
