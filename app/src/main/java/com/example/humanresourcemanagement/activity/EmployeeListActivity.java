package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.adapter.EmployeeAdapter;

import com.example.humanresourcemanagement.databinding.ActivityBangcapBinding;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeListBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import com.example.humanresourcemanagement.R;

public class EmployeeListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter employeeAdapter;
    private ActivityEmployeeListBinding binding;
    private List<Employee> employeeList = new ArrayList<>(); // Sử dụng kiểu Employee thay vì Map
    private firebaseconnet firebaseConnection; // Sửa tên lớp

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        binding = ActivityEmployeeListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));

        employeeAdapter = new EmployeeAdapter(employeeList);
        recyclerViewEmployees.setAdapter(employeeAdapter);
        // Thiết lập sự kiện nhấp vào cho từng item
        employeeAdapter.setOnItemClickListener(employeeId -> {
            Intent intent = new Intent(EmployeeListActivity.this, EmployeeDetailActivity.class);
            intent.putExtra("employeeId", employeeId);
            startActivity(intent);
        });

        

        // Khởi tạo FirebaseConnect
        firebaseConnection = new firebaseconnet(this);
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        // Lấy danh sách nhân viên
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        firebaseConnection.getEmployeeList(new firebaseconnet.OnEmployeeListReceivedListener() {
            @Override
            public void onEmployeeListReceived(List<Employee> employees) {
                employeeList.clear();
                employeeList.addAll(employees);
                employeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEmployeeListError(Exception e) {
                Log.e("EmployeeListActivity", "Error getting employee list: ", e);
            }
        });
    }
}
