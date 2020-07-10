package com.example.trigonousadmin.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.databinding.ActivityOrderListBinding;

public class OrderList extends AppCompatActivity {
    private ActivityOrderListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
