package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.squareup.picasso.Picasso;

public class EmployeeDetailActivity extends AppCompatActivity {

    private TextView tvCCCD, tvGioiTinh,tvMaNV,tvLuong, nameTextView, positionTextView, addressTextView, phoneTextView, birthDateTextView, statusTextView;
    private ImageView profileImageView;
    private firebaseconnet firebaseconnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        // Lấy employeeId từ Intent
        String employeeId = getIntent().getStringExtra("employeeId");

        // Khởi tạo các thành phần UI
        nameTextView = findViewById(R.id.nameTextView);
        positionTextView = findViewById(R.id.positionTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        birthDateTextView = findViewById(R.id.birthDateTextView);
        statusTextView = findViewById(R.id.statusTextView);
        profileImageView = findViewById(R.id.profileImageView);
        tvCCCD = findViewById(R.id.tvCCCD);
        tvLuong = findViewById(R.id.tvLuong);
        tvGioiTinh = findViewById(R.id.tvGioiTinh);
        tvMaNV = findViewById(R.id.tvMaNV);

        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);

        // Gọi hàm lấy thông tin nhân viên
        getEmployeeDetails(employeeId);
    }

    private void getEmployeeDetails(String employeeId) {
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                // Cập nhật UI với thông tin nhân viên
                nameTextView.setText(employee.getName());
                positionTextView.setText(employee.getChucvuId());
                addressTextView.setText(employee.getDiachi());
                phoneTextView.setText(employee.getSdt());
                birthDateTextView.setText(employee.getNgaysinh());
                statusTextView.setText(employee.getTrangthai());
                tvCCCD.setText(employee.getCccd());
                tvLuong.setText(employee.getLuongcoban());
                tvGioiTinh.setText(employee.getGioitinh());
                tvMaNV.setText(employee.getEmployeeId());

                // Hiển thị hình ảnh sử dụng thư viện Picasso
                if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
                    Picasso.get().load(employee.getImageUrl()).into(profileImageView);
                }
            }

            @Override
            public void onEmployeeError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving employee details", e);
            }
        });
    }
}
