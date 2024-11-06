package com.example.humanresourcemanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeDetailBinding;
import com.example.humanresourcemanagement.databinding.HomeLayoutBinding;

public class HomeScreen extends AppCompatActivity {
    private HomeLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.lnNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, EmployeeListActivity.class);
                startActivity(intent);

            }
        });
        binding.lnChucVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, ChucVuActivity.class);
                startActivity(intent);

            }
        });
    }

}