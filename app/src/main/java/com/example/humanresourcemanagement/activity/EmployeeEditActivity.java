package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeEditBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.squareup.picasso.Picasso;

public class EmployeeEditActivity extends AppCompatActivity {

    private ActivityEmployeeEditBinding binding;
    private firebaseconnet firebaseconnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy employeeId từ Intent
        String employeeId = getIntent().getStringExtra("employeeId");

        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);

        // Gọi hàm lấy thông tin nhân viên
        getEmployeeDetails(employeeId);
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        // Lắng nghe sự kiện khi người dùng nhấn nút Lưu
        binding.buttonSave.setOnClickListener(view -> {
            // Gọi hàm cập nhật thông tin nhân viên
            updateEmployeeDetails(employeeId);
        });
    }

    private void getEmployeeDetails(String employeeId) {
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                // Cập nhật UI với thông tin nhân viên
                binding.editTextName.setText(employee.getName());
                binding.editTextChucVuId.setText(employee.getChucvuId());
                binding.editTextDiaChi.setText(employee.getDiachi());
                binding.editTextSDT.setText(employee.getSdt());
                binding.editTextNgaySinh.setText(employee.getNgaysinh());
                binding.editTextNgayBatDau.setText(employee.getNgaybatdau());
                binding.editTextTrangThai.setText(employee.getTrangthai());
                binding.editTextCCCD.setText(employee.getCccd());
                binding.editTextPhongBanId.setText(employee.getPhongbanId());
                binding.editTextLuongCoBan.setText(employee.getLuongcoban());
                binding.editTextGioiTinh.setText(employee.getGioitinh());
                binding.editTextEmployeeId.setText(employee.getEmployeeId());

                // Hiển thị hình ảnh sử dụng thư viện Picasso
                if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
                    Picasso.get().load(employee.getImageUrl()).into(binding.profileImageView);
                }
            }

            @Override
            public void onEmployeeError(Exception e) {
                Log.e("EmployeeEdit", "Error retrieving employee details", e);
            }
        });
    }

    private void updateEmployeeDetails(String employeeId) {
        // Lấy thông tin từ các trường chỉnh sửa
        String name = binding.editTextName.getText().toString();
        String position = binding.editTextChucVuId.getText().toString();
        String address = binding.editTextDiaChi.getText().toString();
        String phone = binding.editTextSDT.getText().toString();
        String birthDate = binding.editTextNgaySinh.getText().toString();
        String status = binding.editTextTrangThai.getText().toString();
        String cccd = binding.editTextCCCD.getText().toString();
        String salary = binding.editTextLuongCoBan.getText().toString();
        String gender = binding.editTextGioiTinh.getText().toString();
        String pb = binding.editTextPhongBanId.getText().toString();
        // Cập nhật đối tượng Employee mới với thông tin chỉnh sửa
        Employee updatedEmployee = new Employee(cccd, position, address, employeeId, gender,
                employeeId, salary, "", name,
                birthDate, birthDate,pb , phone, status);

        // Gọi hàm cập nhật dữ liệu vào Firebase
        firebaseconnet.updateEmployee(employeeId, updatedEmployee, new firebaseconnet.OnEmployeeUpdatedListener() {
            @Override
            public void onEmployeeUpdated() {
                Toast.makeText(EmployeeEditActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish(); // Kết thúc activity sau khi cập nhật thành công
            }

            @Override
            public void onError(Exception e) {
                Log.e("EmployeeEdit", "Error updating employee details", e);
                Toast.makeText(EmployeeEditActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
