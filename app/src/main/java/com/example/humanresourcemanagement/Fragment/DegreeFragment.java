package com.example.humanresourcemanagement.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.activity.AddBangCapNvActivity;
import com.example.humanresourcemanagement.adapter.BangCapNVAdapter;
import com.example.humanresourcemanagement.adapter.SkillNVAdapter;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChiTietBangCap;
import com.example.humanresourcemanagement.model.ChiTietSkill;
import com.example.humanresourcemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;
import com.example.humanresourcemanagement.databinding.FragmentDegreeBinding; // Thêm import cho ViewBinding

public class DegreeFragment extends Fragment {
    private static final int ADD_BANGCAP_REQUEST_CODE = 100;
    private FragmentDegreeBinding binding; // Khai báo biến binding
    private firebaseconnet firebaseconnet;
    private RecyclerView recyclerViewDegrees;
    private BangCapNVAdapter degreeAdapter;
    private List<ChiTietBangCap> chiTietBangCaps = new ArrayList<>();

    public DegreeFragment() {
        // Required empty public constructor
    }

    public static DegreeFragment newInstance(Employee employee) {
        DegreeFragment fragment = new DegreeFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee_data", employee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseconnet = new firebaseconnet(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentDegreeBinding.inflate(inflater, container, false);

        // Initialize RecyclerView and Adapter
        recyclerViewDegrees = binding.recyclerViewDegrees;
        recyclerViewDegrees.setLayoutManager(new LinearLayoutManager(getContext()));
        degreeAdapter = new BangCapNVAdapter(chiTietBangCaps);
        recyclerViewDegrees.setAdapter(degreeAdapter);

        // Retrieve employee data from arguments
        Employee employee = getArguments().getParcelable("employee_data");
        if (employee != null) {
            getBangCapNV(employee.getId()); // Load degrees
        }

        // Set the OnClickListener for the add degree button
        binding.btnAddBC.setOnClickListener(view -> {
            // Chuyển sang màn hình khác
            Intent intent = new Intent(getContext(), AddBangCapNvActivity.class); // Thay thế với Activity muốn chuyển tới
            intent.putExtra("employeeId", employee.getEmployeeId());

            startActivity(intent);
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BANGCAP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getBooleanExtra("shouldRefresh", false)) {
                // Lấy lại employee data từ arguments
                Employee employee = getArguments().getParcelable("employee_data");
                if (employee != null) {
                    // Refresh data
                    chiTietBangCaps.clear();
                    degreeAdapter.notifyDataSetChanged();
                    getBangCapNV(employee.getId());
                }
            }
        }
    }

    // Cập nhật lại phương thức getBangCapNV để xử lý việc refresh tốt hơn
    private void getBangCapNV(String employeeId) {
        firebaseconnet.getBangCapNhanVienById(employeeId, new firebaseconnet.OnBangCapNVListReceivedListener() {
            @Override
            public void onBangCapNVListReceived(List<ChiTietBangCap> receivedBangCapList) {
                if (receivedBangCapList != null && !receivedBangCapList.isEmpty()) {
                    chiTietBangCaps.clear();

                    // Sử dụng một counter để theo dõi khi nào tất cả các bằng cấp đã được xử lý
                    final int[] processedCount = {0};
                    final int totalCount = receivedBangCapList.size();

                    for (ChiTietBangCap bangCap : receivedBangCapList) {
                        String bangcapId = bangCap.getBangcap_id();

                        firebaseconnet.getDegreeName(bangcapId, new firebaseconnet.OnDegreeNameReceivedListener() {
                            @Override
                            public void onDegreeNameReceived(String degreeName) {
                                bangCap.setBangcap_id(degreeName);
                                chiTietBangCaps.add(bangCap);

                                processedCount[0]++;
                                if (processedCount[0] == totalCount) {
                                    // Tất cả bằng cấp đã được xử lý, cập nhật RecyclerView
                                    degreeAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onDegreeNameError(Exception e) {
                                Log.e("EmployeeDetail", "Error retrieving degree name", e);
                                bangCap.setBangcap_id("Không có tên bằng cấp");
                                chiTietBangCaps.add(bangCap);

                                processedCount[0]++;
                                if (processedCount[0] == totalCount) {
                                    degreeAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                } else {
                    chiTietBangCaps.clear();
                    degreeAdapter.notifyDataSetChanged();
                    Log.d("EmployeeDetail", "Không có bằng cấp cho nhân viên");
                }
            }

            @Override
            public void onBangCapNVListError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving degrees", e);
            }
        });
    }
}
