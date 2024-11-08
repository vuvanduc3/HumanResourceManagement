package com.example.humanresourcemanagement.activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.activity.HomeScreen;
import com.example.humanresourcemanagement.activity.MainActivity;
import com.example.humanresourcemanagement.databinding.ActivityLoginBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChucVu;
import com.example.humanresourcemanagement.model.Employee;

public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private firebaseconnet firebaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo firebaseConnection
        firebaseConnection = new firebaseconnet(this);

        // Thiết lập sự kiện cho nút đăng nhập
        binding.loginbutton.setOnClickListener(v -> {
            String employeeId = binding.phoneNumberInput.getText().toString();
            String matKhau = binding.codeInput.getText().toString();

            // Gọi hàm login
            firebaseConnection.login(employeeId, matKhau, new firebaseconnet.OnLoginListener() {
                @Override
                public void onLoginSuccess(Employee employee, String role) {
                    if ("GD".equals(role)) {
                        // Điều hướng tới màn hình GD
                        startActivity(new Intent(Login.this, MainActivity.class));
                    } else if ("TP".equals(role)) {
                        // Điều hướng tới màn hình TP
                        startActivity(new Intent(Login.this, ChucVu.class));
                    } else {
                        // Điều hướng tới màn hình khác
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }
                    finish();
                }

                @Override
                public void onLoginError(Exception e) {
                    Log.e("Login", "Login error: ", e);

                    // Tạo AlertDialog để thông báo lỗi
                    new AlertDialog.Builder(Login.this)
                            .setTitle("Đăng nhập thất bại")
                            .setMessage("Tài khoản hoặc mật khẩu không chính xác. Vui lòng thử lại.")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();
                }

            });
        });
    }
}
