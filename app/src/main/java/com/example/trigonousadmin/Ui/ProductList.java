package com.example.trigonousadmin.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.databinding.ActivityProductListBinding;

public class ProductList extends AppCompatActivity {
    private ActivityProductListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
