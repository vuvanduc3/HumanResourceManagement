package com.example.humanresourcemanagement.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Đảm bảo import Button nếu cần

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.humanresourcemanagement.activity.EmployeeListActivity;
import com.example.humanresourcemanagement.activity.AddThongBaoActivity;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.databinding.FragmentHomeTPhongBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;

import java.util.ArrayList;
import java.util.List;

public class HomeTPhongFragment extends Fragment {
    private FragmentHomeTPhongBinding binding;
    private Employee employee;
    private firebaseconnet firebaseConnection; // Đối tượng để kết nối Firebase
    private List<Employee> employeeList = new ArrayList<>(); // Danh sách nhân viên

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

        // Khởi tạo firebaseConnection
        firebaseConnection = new firebaseconnet(getContext());

        // Ẩn nút thêm nếu chức vụ là "TP"
        binding.tvChaoTP.setText("Chào Trưởng Phòng "+employee.getPhongbanId()+"");
        binding.tvPhongBan.setText("Phòng ban "+employee.getPhongbanId()+"");


        // Lấy danh sách nhân viên
        loadEmployeeData();

        binding.lnNhanVien.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmployeeListActivity.class);
            if (employee != null) {
                intent.putExtra("employee_data", employee);
            }
            startActivity(intent);
        });

        binding.lnThongBao.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddThongBaoActivity.class);
            Log.d("------------------employee", "thong tin: " + employee);
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
                employeeList.clear(); // Xóa danh sách cũ

                // Lấy phongBanId từ employee
                String phongBanId = employee != null ? employee.getPhongbanId() : null;

                // Lọc danh sách nhân viên theo phongBanId
                for (Employee emp : employees) {
                    if (emp.getPhongbanId() != null && emp.getPhongbanId().equals(phongBanId)) {
                        employeeList.add(emp); // Thêm nhân viên vào danh sách nếu phongBanId khớp
                    }
                }

                // Cập nhật số lượng nhân viên vào TextView
                binding.tvTongNV.setText(String.valueOf(employeeList.size()));

            }

            @Override
            public void onEmployeeListError(Exception e) {
                Log.e("HomeTPhongFragment", "Lỗi khi lấy danh sách nhân viên: ", e);
            }
        });
    }
}