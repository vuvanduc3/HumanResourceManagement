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
import com.example.humanresourcemanagement.databinding.HomeLayoutBinding;
import com.example.humanresourcemanagement.activity.EmployeeListActivity;
import com.example.humanresourcemanagement.activity.ChucVuActivity;
import com.example.humanresourcemanagement.activity.BangCapListActivity;
import com.example.humanresourcemanagement.activity.SkillListActivity;
import com.example.humanresourcemanagement.activity.PhongBanListActivity;
import com.example.humanresourcemanagement.model.Employee;

public class HomeFragment extends Fragment {
    private HomeLayoutBinding binding;
    private Employee employee;

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

        // Thêm mã xử lý nếu cần dùng `employee` trong HomeFragment

        binding.lnNhanVien.setOnClickListener(v -> startActivity(new Intent(getActivity(), EmployeeListActivity.class)));
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
}
