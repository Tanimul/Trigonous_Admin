package com.example.trigonousadmin.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.databinding.ActivityProductEntryBinding;

public class ProductEntry extends AppCompatActivity {
    private ActivityProductEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProductEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
