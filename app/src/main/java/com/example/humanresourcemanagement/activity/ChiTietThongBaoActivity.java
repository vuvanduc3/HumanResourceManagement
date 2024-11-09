package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityChitietthongbaoBinding;
import com.example.humanresourcemanagement.model.ThongBao;

public class ChiTietThongBaoActivity extends AppCompatActivity {

    private ActivityChitietthongbaoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChitietthongbaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận thông báo từ Intent
        ThongBao thongBao = getIntent().getParcelableExtra("thongBao");

        if (thongBao != null) {
            // Hiển thị thông tin chi tiết
            binding.tvTieuDe.setText(thongBao.getLoaiThongBao());
            binding.tvNoiDung.setText(thongBao.getThongDiep());
            binding.tvDate.setText(thongBao.getNgayThongBao());
        }

        // Xử lý sự kiện nhấn nút quay lại
        binding.btnBack.setOnClickListener(v -> finish());
    }
}