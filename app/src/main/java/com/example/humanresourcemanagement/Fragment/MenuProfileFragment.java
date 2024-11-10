package com.example.humanresourcemanagement.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.activity.AddThongBaoActivity;
import com.example.humanresourcemanagement.activity.DoiMatKhauActivity;
import com.example.humanresourcemanagement.activity.EmployeeDetailActivity;
import com.example.humanresourcemanagement.activity.EmployeeListActivity;
import com.example.humanresourcemanagement.activity.Login;
import com.example.humanresourcemanagement.databinding.FragmentMenuProfileBinding;
import com.example.humanresourcemanagement.model.Employee;
import com.squareup.picasso.Picasso;

public class MenuProfileFragment extends Fragment {

    private static final String ARG_EMPLOYEE = "employee";
    private Employee employee;
    private FragmentMenuProfileBinding binding;

    public MenuProfileFragment() {
        // Required empty public constructor
    }

    public static MenuProfileFragment newInstance(Employee employee) {
        MenuProfileFragment fragment = new MenuProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EMPLOYEE, employee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            employee = getArguments().getParcelable(ARG_EMPLOYEE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Hiển thị thông tin nhân viên
        binding.profileName.setText(employee.getName());
        binding.profilePosition.setText(employee.getPhongbanId());

        binding.greetingText.setText("Hi "+employee.getName()+"!");

        // Tải hình ảnh
        if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(employee.getImageUrl())
                    .placeholder(R.drawable.baseline_account_circle_24) // Hình ảnh placeholder
                    .error(R.drawable.baseline_account_circle_24) // Hình ảnh lỗi
                    .into(binding.profileImage);
        }

        binding.btnPersonalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmployeeDetailActivity.class);
            intent.putExtra("employeeId", employee.getEmployeeId());
            intent.putExtra("isEditable", false);
            startActivity(intent);
        });

        binding.btnEditPass.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DoiMatKhauActivity.class);
            intent.putExtra("employeeId", employee.getEmployeeId());
            startActivity(intent);
        });

        binding.btnLogOut.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    })
                    .setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}