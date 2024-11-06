package com.example.humanresourcemanagement.activity;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.BangCapAdapter;
import com.example.humanresourcemanagement.firebase.bangcapconnet;
import com.example.humanresourcemanagement.model.BangCap;

import java.util.ArrayList;
import java.util.List;

public class BangCapListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBangCap;
    private BangCapAdapter employeeAdapter;
    private List<BangCap> employeeList = new ArrayList<>(); // Sử dụng kiểu Employee thay vì Map
    private bangcapconnet firebaseConnection; // Sửa tên lớp

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangcap);

        recyclerViewBangCap = findViewById(R.id.recyclerViewBangCap);
        recyclerViewBangCap.setLayoutManager(new LinearLayoutManager(this));

        employeeAdapter = new BangCapAdapter(employeeList);
        recyclerViewBangCap.setAdapter(employeeAdapter);
        // Thiết lập sự kiện nhấp vào cho từng item
        employeeAdapter.setOnItemClickListener(bangcap_id -> {
            Intent intent = new Intent(BangCapListActivity.this, BangCapDetailActivity.class);
            intent.putExtra("bangcap_id", bangcap_id);
            startActivity(intent);
        });

        // Khởi tạo FirebaseConnect
        firebaseConnection = new bangcapconnet(this);

        // Lấy danh sách nhân viên
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        firebaseConnection.getBangCapList(new bangcapconnet.OnBangCapListReceivedListener() {
            @Override
            public void onBangCapListReceived(List<BangCap> employees) {
                employeeList.clear();
                employeeList.addAll(employees);
                employeeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onBangCapListError(Exception e) {

            }

        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Check if the data needs to be reloaded
        if (getIntent().getBooleanExtra("reload", false)) {
            loadData(); // Method to reload the list data
        }
    }

}
