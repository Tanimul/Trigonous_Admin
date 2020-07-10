package com.example.trigonousadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.trigonousadmin.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.facebook.setOnClickListener(this);
        binding.email.setOnClickListener(this);
        binding.instragram.setOnClickListener(this);
        binding.web.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.facebook) {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if FB is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/trigonous/")));  //Trys to make intent with FB's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/trigonous/"))); //catches and opens a url to the desired page
            }
        } else if (v.getId() == R.id.email) {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if email is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mail.google.com/mail/u/0/#inbox"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mail.google.com/mail/u/0/#inbox"))); //catches and opens a url to the desired page
            }

        } else if (v.getId() == R.id.instragram) {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if Instagram is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/trigonous_bd/"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.www.instagram.com/trigonous_bd/"))); //catches and opens a url to the desired page
            }
        } else {
            Toast.makeText(this, "added twitter link for testing purpose", Toast.LENGTH_SHORT).show();
            try {
                getPackageManager()
                        .getPackageInfo("com.example.nirmol_nogori", 0); //Checks if github is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/Trigonous14"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/Trigonous14"))); //catches and opens a url to the desired page
            }
        }
    }
}
