package com.example.trigonousadmin.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.databinding.ActivityCustomerListBinding;

public class CustomerList extends AppCompatActivity {
    private ActivityCustomerListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCustomerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
