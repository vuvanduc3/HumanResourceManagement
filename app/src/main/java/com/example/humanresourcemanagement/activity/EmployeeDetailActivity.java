package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.EmployeeDetailAdapter;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeDetailBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

public class EmployeeDetailActivity extends AppCompatActivity {
    private int RQ_CODE = 9999;
    private static final int ADD_BANGCAP_REQUEST = 1;
    private static final int ADD_SKILL_REQUEST = 2;
    private ActivityEmployeeDetailBinding binding;
    private firebaseconnet firebaseconnet;
    private String cccdID;
    private Employee employeeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy employeeId từ Intent
        String employeeId = getIntent().getStringExtra("employeeId");
        boolean isEditable = getIntent().getBooleanExtra("isEditable", false);

        if(!isEditable){
            binding.btnCall.setVisibility(View.INVISIBLE);
            binding.tvEditNV.setVisibility(View.INVISIBLE);
        }
        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);

        // Gọi hàm lấy thông tin nhân viên
        getEmployeeDetails(employeeId);

        // Quay lại màn hình trước
        binding.btnBack.setOnClickListener(v -> finish());

        // Sự kiện khi nhấn vào nút chỉnh sửa nhân viên
        binding.tvEditNV.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeDetailActivity.this, EmployeeEditActivity.class);
            intent.putExtra("employeeId", employeeId);
            startActivity(intent);
        });
        // Sự kiện khi nhấn vào nút gọi điện
        binding.btnCall.setOnClickListener(v -> {
            if (employeeInfo != null && employeeInfo.getSdt() != null) {
                String phoneNumber = employeeInfo.getSdt();
                makeCall(phoneNumber);
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                callIntent.setData(Uri.parse("tel:" + phoneNumber));
//                startActivity(callIntent);
            }
        });
    }

    // Thiết lập TabLayout và ViewPager2
    private void setupTabsAndViewPager() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Tạo Adapter cho ViewPager2
        EmployeeDetailAdapter adapter = new EmployeeDetailAdapter(this, employeeInfo);
        viewPager.setAdapter(adapter);

        // Liên kết TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Thông tin");
                    break;
                case 1:
                    tab.setText("Bằng cấp");
                    break;
                case 2:
                    tab.setText("Kỹ năng");
                    break;
            }
        }).attach();
    }

    private void getEmployeeDetails(String employeeId) {
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                employeeInfo = employee;
                cccdID = employee.getCccd();
                binding.nameTextViewInfo.setText(employee.getName());
                if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
                    Picasso.get().load(employee.getImageUrl()).into(binding.profileImageView);
                }

                // Set up TabLayout và ViewPager2 sau khi nhận được employeeInfo
                setupTabsAndViewPager();
            }

            @Override
            public void onEmployeeError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving employee details", e);
            }
        });
    }
    private void makeCall(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Thực hiện cuộc gọi
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } else {
            // Xin quyền nếu chưa có
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RQ_CODE && permissions.length == grantResults.length){
            // kiểm tra toàn bộ kết qủa grantResult

            for(int i = 0; i < grantResults.length; ++i){
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    return;
                }
            }


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String employeeId = getIntent().getStringExtra("employeeId");

            // Code để xử lý kết quả trả về từ hoạt động thêm bằng cấp hoặc kỹ năng (nếu cần)
        }
    }
}
