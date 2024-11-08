package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeDetailBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.squareup.picasso.Picasso;

public class EmployeeDetailActivity extends AppCompatActivity {

    private ActivityEmployeeDetailBinding binding;
    private firebaseconnet firebaseconnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy employeeId từ Intent
        String employeeId = getIntent().getStringExtra("employeeId");

        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);

        // Gọi hàm lấy thông tin nhân viên
        getEmployeeDetails(employeeId);

        binding.tvEditNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeDetailActivity.this, EmployeeEditActivity.class);
                intent.putExtra("employeeId", employeeId);
                startActivity(intent);
            }
        });
    }

    private void getEmployeeDetails(String employeeId) {
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                // Cập nhật UI với thông tin nhân viên
                binding.nameTextView.setText(employee.getName());
                binding.positionTextView.setText(employee.getChucvuId());
                binding.addressTextView.setText(employee.getDiachi());
                binding.phoneTextView.setText(employee.getSdt());
                binding.birthDateTextView.setText(employee.getNgaysinh());
                binding.statusTextView.setText(employee.getTrangthai());
                binding.tvCCCD.setText(employee.getCccd());
                binding.tvLuong.setText(employee.getLuongcoban());
                binding.tvGioiTinh.setText(employee.getGioitinh());
                binding.tvMaNV.setText(employee.getEmployeeId());

                // Hiển thị hình ảnh sử dụng thư viện Picasso
                if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
                    Picasso.get().load(employee.getImageUrl()).into(binding.profileImageView);
                }
            }

            @Override
            public void onEmployeeError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving employee details", e);
            }
        });
    }
}
