package com.example.humanresourcemanagement.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.humanresourcemanagement.activity.AddThongBaoActivity;
import com.example.humanresourcemanagement.adapter.EmployeeAdapter;
import com.example.humanresourcemanagement.databinding.HomeLayoutBinding;
import com.example.humanresourcemanagement.activity.EmployeeListActivity;
import com.example.humanresourcemanagement.activity.ChucVuActivity;
import com.example.humanresourcemanagement.activity.BangCapListActivity;
import com.example.humanresourcemanagement.activity.SkillListActivity;
import com.example.humanresourcemanagement.activity.PhongBanListActivity;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.firebase.phongBanfirebase;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.PhongBan;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomeLayoutBinding binding;
    private Employee employee;
    private firebaseconnet firebaseConnection;
    private phongBanfirebase phongBanfirebase;
    private List<Employee> employeeList = new ArrayList<>(); // Khởi tạo danh sách nhân viên
    private List<PhongBan> phongBanList = new ArrayList<>();
    // Phương thức tạo instance với Employee
    public static HomeFragment newInstance(Employee employee) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee_data", employee); // Truyền Employee qua Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nhận Employee từ Bundle
        if (getArguments() != null) {
            employee = getArguments().getParcelable("employee_data");
        }

        // Khởi tạo firebaseConnection
        firebaseConnection = new firebaseconnet(getContext());
        phongBanfirebase = new phongBanfirebase(getContext());
        // Thiết lập các OnClickListener cho các nút
        setupClickListeners();

        loadEmployeeData();
        loadDSPhongBanData();


    }

    private void setupClickListeners() {
        binding.lnNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmployeeListActivity.class);
            if (employee != null) {
                intent.putExtra("employee_data", employee);
            }
            startActivity(intent);
        });

        binding.lnChucVu.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChucVuActivity.class)));
        binding.lnBangCap.setOnClickListener(v -> startActivity(new Intent(getActivity(), BangCapListActivity.class)));
        binding.lnSkill.setOnClickListener(v -> startActivity(new Intent(getActivity(), SkillListActivity.class)));
        binding.lnPhongBan.setOnClickListener(v -> startActivity(new Intent(getActivity(), PhongBanListActivity.class)));

        binding.lnThongBao.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddThongBaoActivity.class);
            // Truyền Employee vào Intent
            if (employee != null) {
                intent.putExtra("employee_data", employee);
            }
            startActivity(intent);
        });
    }

    private void loadEmployeeData() {
        firebaseConnection.getEmployeeList(new firebaseconnet.OnEmployeeListReceivedListener() {
            @Override
            public void onEmployeeListReceived(List<Employee> employees) {
                employeeList.clear();
                employeeList.addAll(employees);
                binding.tvTongNV.setText(employeeList.size()+"");
            }

            @Override
            public void onEmployeeListError(Exception e) {
                Log.e("HomeFragment", "Error getting employee list: ", e);
            }
        });
    }




    private void loadDSPhongBanData() {
        phongBanfirebase.getPhongBanList(new phongBanfirebase.OnPhongBanListReceivedListener() {
            @Override
            public void onPhongBanListReceived(List<PhongBan> phongBans) {
                phongBanList.clear();
                 phongBanList.addAll(phongBans);
                binding.tvTongPB.setText(phongBanList.size()+"");
            }

            @Override
            public void onPhongBanListError(Exception e) {
                // Xử lý lỗi nếu cần
            }
        });
    }

}