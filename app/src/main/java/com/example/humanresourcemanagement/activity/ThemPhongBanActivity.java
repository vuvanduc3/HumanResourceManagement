package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.databinding.ActivityThemphongbanBinding;
import com.example.humanresourcemanagement.firebase.phongBanfirebase;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.PhongBan;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;
public class ThemPhongBanActivity extends AppCompatActivity {

    private ActivityThemphongbanBinding binding;
    private phongBanfirebase phongBanfirebase;
    private firebaseconnet firebaseConnectionEmployee;
    private List<Employee> employeeList = new ArrayList<>();
    private ArrayAdapter<Employee> employeeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemphongbanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy mã phòng ban từ Intent
        String maPhongBan = getIntent().getStringExtra("maPhongBan");

        // Khởi tạo Firebase
        phongBanfirebase = new phongBanfirebase(this);
        firebaseConnectionEmployee = new firebaseconnet(this);

        loadEmployeeList();

        // Quay lại màn hình trước
        binding.btnBack.setOnClickListener(v -> finish());


        //Thêm phòng van

        binding.btnAdd.setOnClickListener(v->addPhongBan());


    }


    // thêm phòng ban
    private void addPhongBan() {
        String maPhongBan = binding.tvMaPB.getText().toString().trim();  // Lấy mã phòng ban từ EditText
        String tenPhongBan = binding.tvTenPB.getText().toString().trim(); // Lấy tên phòng ban từ EditText
        Employee selectedEmployee = (Employee) binding.spinnerTruongPhong.getSelectedItem(); // Lấy nhân viên đã chọn

        if (maPhongBan.isEmpty() || tenPhongBan.isEmpty() ) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng PhongBan
        PhongBan phongBan = new PhongBan();
        phongBan.setMaPhongBan(maPhongBan);
        phongBan.setTenPhongBan(tenPhongBan);
        if(selectedEmployee != null)
        {
            phongBan.setMaQuanLy(selectedEmployee.getEmployeeId());
        }


        // Gọi phương thức thêm phòng ban vào Firebase
        phongBanfirebase.addPhongBan(phongBan, new phongBanfirebase.OnPhongBanAddedListener() {
            @Override
            public void onPhongBanAdded() {
                Toast.makeText(ThemPhongBanActivity.this, "Thêm phòng ban thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onPhongBanAddError(Exception e) {
                Toast.makeText(ThemPhongBanActivity.this, "Có lỗi xảy ra khi thêm phòng ban.", Toast.LENGTH_SHORT).show();
                Log.e("ThemPhongBanActivity", "Error adding phong ban", e);
            }
        });


        if(selectedEmployee != null)
        {
            // Cập nhật chức vụ cho trưởng phòng
            phongBanfirebase.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "TP",maPhongBan, new phongBanfirebase.OnEmployeeUpdateListener() {
                @Override
                public void onEmployeeUpdated() {
                }

                @Override
                public void onEmployeeUpdateError(Exception e) {
                    Log.e("PhongBanDetail", "Error updating employee chucVuId", e);
                    Toast.makeText(ThemPhongBanActivity.this, "Có lỗi xảy ra khi cập nhật chức vụ nhân viên.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



// lấy ds nhân viên cho spinner
    private void loadEmployeeList() {
        firebaseConnectionEmployee.getEmployeeList(new firebaseconnet.OnEmployeeListReceivedListener() {
            @Override
            public void onEmployeeListReceived(List<Employee> employees) {
                employeeList.clear();
                for (Employee employee : employees) {
                    if (!employee.getChucvuId().equals("GD") && !employee.getChucvuId().equals("TP")) {
                        employeeList.add(employee);
                    }
                }

                setupEmployeeSpinner(); // Thiết lập spinner với danh sách đã lọc
                Log.d("-------------ds-------", "onEmployeeListReceived: "+employeeList);

            }


            @Override
            public void onEmployeeListError(Exception e) {
                Log.e("PhongBanDetail", "Error fetching employee list", e);
            }
        });

    }
    private void setupEmployeeSpinner() {
        employeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employeeList);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTruongPhong.setAdapter(employeeAdapter);


    }







}