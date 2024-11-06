package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.adapter.PhongBanAdapter;
import com.example.humanresourcemanagement.firebase.phongBanfirebase;
import com.example.humanresourcemanagement.firebase.firebaseconnet;

import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.PhongBan;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityPhongbanListBinding; // Import binding class

import java.util.ArrayList;
import java.util.List;

public class PhongBanListActivity extends AppCompatActivity {

    private ActivityPhongbanListBinding binding; // Tạo biến Binding
    private PhongBanAdapter phongBanAdapter;
    private List<PhongBan> phongBanList = new ArrayList<>();
    private phongBanfirebase firebaseConnection;
    private firebaseconnet firebaseConnectionEmployee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo FirebaseConnect
        firebaseConnection = new phongBanfirebase(this);
        firebaseConnectionEmployee = new firebaseconnet(this);

        // Thay thế setContentView() bằng binding
        binding = ActivityPhongbanListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewPhongBan.setLayoutManager(new LinearLayoutManager(this));

        phongBanAdapter = new PhongBanAdapter(phongBanList);
        binding.recyclerViewPhongBan.setAdapter(phongBanAdapter);

        // Thiết lập sự kiện nhấp vào cho từng item
        phongBanAdapter.setOnItemClickListener(maPhongBan -> {
            Intent intent = new Intent(PhongBanListActivity.this, PhongBanDetailActivity.class);
            intent.putExtra("maPhongBan", maPhongBan);

            Log.d("maPB---------------------------", "onCreate: "+maPhongBan);
            startActivity(intent);
        });

        //xử lý nút quay lại
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    // Lấy danh sách pb
    @Override
    protected void onResume() {
        super.onResume();
        loadDSPhongBanData();
    }

    private void loadDSPhongBanData() {
        firebaseConnection.getPhongBanList(new phongBanfirebase.OnPhongBanListReceivedListener() {
            @Override
            public void onPhongBanListReceived(List<PhongBan> phongBans) {
                phongBanList.clear();
                // Hàm để lấy tên trưởng phòng
                for (PhongBan phongBan : phongBans) {
                    String maTP = phongBan.getMaQuanLy();
                    // Gọi Firebase để lấy tên trưởng phòng
                    if (maTP == null || maTP.isEmpty()) {

                        phongBanList.add(phongBan);
                        phongBanAdapter.notifyDataSetChanged();
                    } else {
                        // Gọi Firebase để lấy tên trưởng phòng
                        firebaseConnectionEmployee.getEmployeeById(maTP, new firebaseconnet.OnEmployeeReceivedListener() {
                            @Override
                            public void onEmployeeReceived(Employee employee) {
                                String tenTP = "";
                                if (employee != null) {
                                    tenTP = employee.getName();
                                }
                                phongBan.setMaQuanLy(tenTP);
                                phongBanList.add(phongBan);
                                phongBanAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onEmployeeError(Exception e) {
                            }
                        });
                    }
                ;

                }
            }

            @Override
            public void onPhongBanListError(Exception e) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng resources nếu cần
        binding = null; // Giải phóng binding khi không dùng nữa
    }
}