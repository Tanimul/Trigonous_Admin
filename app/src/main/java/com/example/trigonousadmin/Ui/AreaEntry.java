package com.example.trigonousadmin.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trigonousadmin.MainActivity;
import com.example.trigonousadmin.databinding.ActivityAreaEntryBinding;

public class AreaEntry extends AppCompatActivity {
    private ActivityAreaEntryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAreaEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
