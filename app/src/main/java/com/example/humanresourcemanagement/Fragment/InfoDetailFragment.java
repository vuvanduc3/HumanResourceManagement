package com.example.humanresourcemanagement.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.databinding.FragmentInfoDetailBinding;

public class InfoDetailFragment extends Fragment {

    private FragmentInfoDetailBinding binding;
    private Employee employee;

    public InfoDetailFragment() {
        // Required empty public constructor
    }

    public static InfoDetailFragment newInstance(Employee employee) {

        InfoDetailFragment fragment = new InfoDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee_data", employee);  // Truyền Employee qua Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employee = getArguments().getParcelable("employee_data");  // Lưu Employee vào biến instance
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (employee != null) {
            updateUI(employee);  // Cập nhật UI sau khi View đã được tạo
        }
    }

    private void updateUI(Employee employee) {
        binding.tvMaNV.setText(employee.getEmployeeId());
        binding.tvCCCD.setText(employee.getCccd());
        binding.tvGioiTinh.setText(employee.getGioitinh());
        binding.positionTextView.setText(employee.getChucvuId());
        binding.addressTextView.setText(employee.getDiachi());
        binding.phoneTextView.setText(employee.getSdt());
        binding.tvLuong.setText(employee.getLuongcoban());
        binding.birthDateTextView.setText(employee.getNgaysinh());
        binding.statusTextView.setText(employee.getTrangthai());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
