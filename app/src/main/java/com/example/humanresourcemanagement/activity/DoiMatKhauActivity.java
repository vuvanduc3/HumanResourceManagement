package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityDoiMatKhauBinding; // Import binding
import com.example.humanresourcemanagement.firebase.employeeFirebase;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;

public class DoiMatKhauActivity extends AppCompatActivity {

    private ActivityDoiMatKhauBinding binding;
    private employeeFirebase firebaseConnect;
    private firebaseconnet firebaseconnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoiMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo kết nối Firebase
        firebaseConnect = new employeeFirebase(this);
        firebaseconnet = new firebaseconnet(this);

        // Thiết lập sự kiện cho nút Đổi mật khẩu
        binding.loginbutton.setOnClickListener(v -> changePassword());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void changePassword() {
        String currentPass = binding.phoneNumberInput.getText().toString().trim();
        String newPass = binding.edtNewPass.getText().toString().trim();
        String reNewPass = binding.edtReNewPass.getText().toString().trim();

        // Kiểm tra tính hợp lệ của các trường nhập
        if (currentPass.isEmpty()) {
            showAlertDialog("Thông báo", "Vui lòng nhập mật khẩu hiện tại");
            return;
        }
        if (newPass.isEmpty()) {
            showAlertDialog("Thông báo", "Vui lòng nhập mật khẩu mới");
            return;
        }
        if (reNewPass.isEmpty()) {
            showAlertDialog("Thông báo", "Vui lòng nhập lại mật khẩu mới");
            return;
        }
        if (!newPass.equals(reNewPass)) {
            showAlertDialog("Thông báo", "Mật khẩu mới không khớp");
            return;
        }

        String employeeId = getIntent().getStringExtra("employeeId");

        // Lấy thông tin nhân viên
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                // Kiểm tra mật khẩu hiện tại
                if (employee.getMatKhau().equals(currentPass)) {
                    // Nếu mật khẩu hiện tại khớp, thực hiện đổi mật khẩu
                    firebaseConnect.changePassword(employeeId, currentPass, newPass, new employeeFirebase.OnPasswordChangeListener() {
                        @Override
                        public void onPasswordChangeSuccess() {
                            Toast.makeText(DoiMatKhauActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            finish(); // Đóng activity sau khi đổi mật khẩu
                        }

                        @Override
                        public void onPasswordChangeError(Exception e) {
                            showAlertDialog("Thông báo", "Đổi mật khẩu không thành công: " + e.getMessage());
                        }
                    });
                } else {
                    showAlertDialog("Thông báo", "Mật khẩu hiện tại không chính xác");
                }
            }

            @Override
            public void onEmployeeError(Exception e) {
                showAlertDialog("Thông báo", "Lỗi lấy thông tin nhân viên: " + e.getMessage());
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}