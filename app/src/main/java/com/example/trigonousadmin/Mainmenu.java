package com.example.trigonousadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.trigonousadmin.databinding.ActivityMainmenuBinding;


public class Mainmenu extends AppCompatActivity {
    private ActivityMainmenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainmenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}
