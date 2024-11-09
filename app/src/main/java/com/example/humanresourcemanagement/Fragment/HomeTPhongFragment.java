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

import com.example.humanresourcemanagement.activity.EmployeeListActivity;
import com.example.humanresourcemanagement.activity.AddThongBaoActivity;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.databinding.FragmentHomeTPhongBinding;

public class HomeTPhongFragment extends Fragment {
    private FragmentHomeTPhongBinding binding;
    private Employee employee;

    // Phương thức tạo instance với Employee
    public static HomeTPhongFragment newInstance(Employee employee) {
        HomeTPhongFragment fragment = new HomeTPhongFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee_data", employee); // Truyền Employee qua Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeTPhongBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Nhận Employee từ Bundle
        if (getArguments() != null) {
            employee = getArguments().getParcelable("employee_data");
        }

        // Sử dụng Employee nếu cần
        if (employee != null) {
            // Ví dụ: làm gì đó với employee
        }

        binding.lnNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmployeeListActivity.class);
            startActivity(intent);
        });

        binding.lnThongBao.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddThongBaoActivity.class);


            Log.d("------------------eployeee", "thong tind: "+employee);
            // Truyền Employee vào Intent
            if (employee != null) {
                intent.putExtra("employee_data", employee);
            }
            startActivity(intent);
        });
    }
}