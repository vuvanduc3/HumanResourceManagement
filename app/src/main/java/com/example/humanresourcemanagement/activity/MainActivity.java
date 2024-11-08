package com.example.humanresourcemanagement.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.adapter.EmployeeAdapter;
import com.example.humanresourcemanagement.firebase.firebaseconnet;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> employeeList = new ArrayList<>(); // Sử dụng kiểu Employee thay vì Map
    private firebaseconnet firebaseConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.bottom_layout);


    }
}