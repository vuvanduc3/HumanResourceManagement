package com.example.humanresourcemanagement.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.ThongBaoAdapter;
import com.example.humanresourcemanagement.firebase.thongBaoFirebase;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.ThongBao;

import java.util.ArrayList;
import java.util.List;

public class NotifiListFragment extends Fragment {

    private static final String ARG_EMPLOYEE = "employee_arg"; // Key cho đối tượng Employee

    private ListView listView;
    private ThongBaoAdapter adapter;
    private List<ThongBao> thongBaoList;
    private thongBaoFirebase thongBaoService; // Đối tượng để gọi phương thức lấy thông báo
    private Employee employee;

    // Phương thức tạo mới với Employee
    public static NotifiListFragment newInstance(Employee employee) {
        NotifiListFragment fragment = new NotifiListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EMPLOYEE, employee); // Đẩy đối tượng Employee vào Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.activity_notifice_screen, container, false);

        // Nhận đối tượng Employee từ Bundle
        if (getArguments() != null) {
            employee = getArguments().getParcelable(ARG_EMPLOYEE);
        }

        // Khởi tạo view và Firebase
        listView = view.findViewById(R.id.listViewNotifications);
        thongBaoList = new ArrayList<>();
        adapter = new ThongBaoAdapter(getContext(), thongBaoList);
        listView.setAdapter(adapter);

        // Khởi tạo dịch vụ thongBaoFirebase
        thongBaoService = new thongBaoFirebase(getContext());

        // Lắng nghe danh sách thông báo
        listenForNotifications();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("NotifiListFragment---", "onResume called------------------------");
        // Không cần gọi fetchNotifications ở đây nữa vì đã lắng nghe thông báo
    }

    // Phương thức để lắng nghe danh sách thông báo từ Firebase
    private void listenForNotifications() {
        thongBaoService.listenForThongBaos(new thongBaoFirebase.OnThongBaosRetrievedListener() {
            @Override
            public void onThongBaosRetrieved(List<ThongBao> thongBaoListReceived) {
                thongBaoList.clear(); // Xóa danh sách cũ

                String maNhanVien = employee.getEmployeeId(); // Lấy mã nhân viên từ employee

                // Lọc danh sách thông báo
                for (ThongBao thongBao : thongBaoListReceived) {
                    if (thongBao.getMaNhanVien().equals(maNhanVien)) {
                        thongBaoList.add(0, thongBao); // Thêm vào đầu danh sách
                    }
                }

                adapter.notifyDataSetChanged(); // Cập nhật adapter
            }

            @Override
            public void onThongBaoRetrieveError(Exception e) {
                Toast.makeText(getContext(), "Lỗi khi lấy thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}