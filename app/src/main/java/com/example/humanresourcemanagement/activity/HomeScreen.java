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

        binding.lnNhanVien.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, EmployeeListActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện click vào biểu tượng Thông Báo
        binding.lnThongBao.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, NotificeScreen.class);
            startActivity(intent);
        });
    }
}
