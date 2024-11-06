package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.databinding.ActivityPhongbanDetailBinding;
import com.example.humanresourcemanagement.firebase.phongBanfirebase;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.PhongBan;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDetailActivity extends AppCompatActivity {

    private ActivityPhongbanDetailBinding binding;
    private phongBanfirebase phongBanfirebase;
    private firebaseconnet firebaseConnectionEmployee;
    private List<Employee> employeeList = new ArrayList<>();
    private ArrayAdapter<Employee> employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhongbanDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy mã phòng ban từ Intent
        String maPhongBan = getIntent().getStringExtra("maPhongBan");

        // Khởi tạo Firebase
        phongBanfirebase = new phongBanfirebase(this);
        firebaseConnectionEmployee = new firebaseconnet(this);

        // Gọi hàm lấy thông tin phòng ban
        getPhongBanDetails(maPhongBan);

        // Xóa phòng ban
        binding.btnDelete.setOnClickListener(v -> deletePhongBan(maPhongBan));

        // Quay lại màn hình trước
        binding.btnBack.setOnClickListener(v -> finish());

        // Sự kiện nhấn nút "Edit"
        binding.btnEdit.setOnClickListener(v -> enableEditMode());

        // Sự kiện nhấn nút "Save"
//        binding.btnSave.setOnClickListener(v -> savePhongBan(maPhongBan));
    }

    private void getPhongBanDetails(String maPhongBan) {
        phongBanfirebase.getPhongBanById(maPhongBan, new phongBanfirebase.OnPhongBanReceivedListener() {
            @Override
            public void onPhongBanReceived(PhongBan phongBan) {
                binding.tvMaPB.setText(phongBan.getMaPhongBan());
                binding.tvTenPB.setText(phongBan.getTenPhongBan());

                String maQuanLy = phongBan.getMaQuanLy();
                if (maQuanLy != null && !maQuanLy.isEmpty()) {
                    firebaseConnectionEmployee.getEmployeeById(maQuanLy, new firebaseconnet.OnEmployeeReceivedListener() {
                        @Override
                        public void onEmployeeReceived(Employee employee) {
                            if (employee != null) {
                                binding.tvTenTP.setText(employee.getName());
                            } else {
                                binding.tvTenTP.setText("Không tìm thấy trưởng phòng");
                            }
                        }

                        @Override
                        public void onEmployeeError(Exception e) {
                            Log.e("PhongBanDetail", "Error fetching employee details", e);
                            binding.tvTenTP.setText("Không tìm thấy trưởng phòng");
                        }
                    });
                }

                // Lấy danh sách nhân viên cho spinner
                loadEmployeeList();
            }

            @Override
            public void onPhongBanError(Exception e) {
                Log.e("PhongBanDetail", "Error fetching phong ban details", e);
            }
        });
    }

    private void loadEmployeeList() {
        firebaseConnectionEmployee.getEmployeeList(new firebaseconnet.OnEmployeeListReceivedListener() {
            @Override
            public void onEmployeeListReceived(List<Employee> employees) {
                employeeList.clear();
                employeeList.addAll(employees);
                setupEmployeeSpinner(); // Thiết lập spinner
                Log.d("dsnv-----------------", "onEmployeeListReceived: " + employees);
            }

            @Override
            public void onEmployeeListError(Exception e) {
                Log.e("PhongBanDetail", "Error fetching employee list", e);
            }
        });

    }

    private void setupEmployeeSpinner() {
        // Thiết lập ArrayAdapter cho spinner
        employeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employeeList);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTruongPhong.setAdapter(employeeAdapter);
    }

    private void enableEditMode() {
        // Cho phép chỉnh sửa tên phòng ban
        binding.tvTenPB.setEnabled(true);
        binding.tvTenPB.setFocusableInTouchMode(true);
        binding.tvTenPB.requestFocus();
        binding.tvTenPB.setSelection(binding.tvTenPB.getText().length());
        // Hiện spinner và nút lưu
        binding.spinnerTruongPhong.setVisibility(View.VISIBLE);
        binding.btnSave.setVisibility(View.VISIBLE);
    }



    private void deletePhongBan(String maPhongBan) {
        phongBanfirebase.deletePhongBanById(maPhongBan, new phongBanfirebase.OnPhongBanDeleteListener() {
            @Override
            public void onPhongBanDeleted() {
                Toast.makeText(PhongBanDetailActivity.this, "Xóa phòng ban thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Trở lại màn hình trước đó hoặc đến một màn hình hiển thị danh sách phòng ban
            }

            @Override
            public void onPhongBanDeleteError(Exception e) {
                Log.e("PhongBanDetail", "Error deleting phong ban", e);
                Toast.makeText(PhongBanDetailActivity.this, "Có lỗi xảy ra khi xóa phòng ban.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}