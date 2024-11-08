package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.humanresourcemanagement.R;
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
        binding.lnBangCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, BangCapListActivity.class);
                startActivity(intent);

            }
        });
        binding.lnSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, SkillListActivity.class);
                startActivity(intent);
            }
        });

        binding.lnPhongBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, PhongBanListActivity.class);
                startActivity(intent);
            }
        });
    }
}
