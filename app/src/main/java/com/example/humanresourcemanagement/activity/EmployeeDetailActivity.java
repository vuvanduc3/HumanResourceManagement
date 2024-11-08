package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.BangCapNVAdapter;
import com.example.humanresourcemanagement.adapter.EmployeeAdapter;

import com.example.humanresourcemanagement.adapter.SkillNVAdapter;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeDetailBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.firebase.bangcapconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.ChiTietBangCap;
import com.example.humanresourcemanagement.model.ChiTietSkill;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDetailActivity extends AppCompatActivity {

    private ActivityEmployeeDetailBinding binding;
    private firebaseconnet firebaseconnet;
    private RecyclerView recyclerViewDegrees; // RecyclerView for degrees
    private BangCapNVAdapter degreeAdapter; // Adapter for degrees
    private List<ChiTietBangCap> chiTietBangCaps = new ArrayList<>();
    private RecyclerView recyclerViewSkillnv; // RecyclerView for degrees
    private SkillNVAdapter Skillnvdapter; // Adapter for degrees
    private List<ChiTietSkill> chiTietSkillList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy employeeId từ Intent
        String employeeId = getIntent().getStringExtra("employeeId");

        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);

        // Gọi hàm lấy thông tin nhân viên
        getEmployeeDetails(employeeId);

        // Lấy thông tin bằng cấp
        getBangCapNV(employeeId);
        getSkillNV(employeeId);
        // Quay lại màn hình trước
        binding.btnBack.setOnClickListener(v -> finish());

        // Chỉnh sửa thông tin nhân viên
        binding.tvEditNV.setOnClickListener(view -> {
            Intent intent = new Intent(EmployeeDetailActivity.this, EmployeeEditActivity.class);
            intent.putExtra("employeeId", employeeId);
            startActivity(intent);
        });

        // Initialize RecyclerView for degrees
        recyclerViewDegrees = findViewById(R.id.recyclerViewDegrees);
        recyclerViewDegrees.setLayoutManager(new LinearLayoutManager(this));
        degreeAdapter = new BangCapNVAdapter(chiTietBangCaps);
        recyclerViewDegrees.setAdapter(degreeAdapter);

        recyclerViewSkillnv = findViewById(R.id.recyclerViewSkillNV);
        recyclerViewSkillnv.setLayoutManager(new LinearLayoutManager(this));
        Skillnvdapter = new SkillNVAdapter(chiTietSkillList);
        recyclerViewSkillnv.setAdapter(Skillnvdapter);
    }

    // Lấy thông tin nhân viên từ Firebase
    private void getEmployeeDetails(String employeeId) {
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                // Cập nhật UI với thông tin nhân viên
                binding.nameTextView.setText(employee.getName());
                binding.positionTextView.setText(employee.getChucvuId());
                binding.addressTextView.setText(employee.getDiachi());
                binding.phoneTextView.setText(employee.getSdt());
                binding.birthDateTextView.setText(employee.getNgaysinh());
                binding.statusTextView.setText(employee.getTrangthai());
                binding.tvCCCD.setText(employee.getCccd());
                binding.tvLuong.setText(employee.getLuongcoban());
                binding.tvGioiTinh.setText(employee.getGioitinh());
                binding.tvMaNV.setText(employee.getEmployeeId());

                // Hiển thị hình ảnh sử dụng thư viện Picasso
                if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
                    Picasso.get().load(employee.getImageUrl()).into(binding.profileImageView);
                }
            }

            @Override
            public void onEmployeeError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving employee details", e);
            }
        });
    }

    // Lấy thông tin bằng cấp nhân viên từ Firebase
    private void getBangCapNV(String employeeId) {
        firebaseconnet.getBangCapNhanVienById(employeeId, new firebaseconnet.OnBangCapNVListReceivedListener() {
            @Override
            public void onBangCapNVListReceived(List<ChiTietBangCap> receivedBangCapList) {
                // Kiểm tra nếu có thông tin bằng cấp
                if (receivedBangCapList != null && !receivedBangCapList.isEmpty()) {
                    // Clear existing data in the list
                    chiTietBangCaps.clear();

                    // Loop through the received list of degrees and fetch their names
                    for (ChiTietBangCap bangCap : receivedBangCapList) {
                        String bangcapId = bangCap.getBangcap_id(); // Get the degree ID

                        // Fetch the degree name from Firebase using the degree ID
                        firebaseconnet.getDegreeName(bangcapId, new firebaseconnet.OnDegreeNameReceivedListener() {
                            @Override
                            public void onDegreeNameReceived(String degreeName) {
                                // Set the degree name in the ChiTietBangCap object
                                bangCap.setBangcap_id(degreeName);  // Update the degree name

                                // Add the updated degree to the list
                                chiTietBangCaps.add(bangCap);

                                // Notify the adapter to refresh the data
                                degreeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDegreeNameError(Exception e) {
                                Log.e("EmployeeDetail", "Error retrieving degree name", e);
                                bangCap.setBangcap_id("Không có tên bằng cấp"); // If there's an error, set default text
                                chiTietBangCaps.add(bangCap); // Add to the list even if there is an error
                                degreeAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Log.d("EmployeeDetail", "Không có bằng cấp cho nhân viên");
                }
            }

            @Override
            public void onBangCapNVListError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving degrees", e);
            }
        });
    }

    // Lấy thông tin bằng cấp nhân viên từ Firebase
    private void getSkillNV(String employeeId) {
        firebaseconnet.getSkillNhanVienById(employeeId, new firebaseconnet.OnSkillNVListReceivedListener() {
            @Override
            public void onSkillNVListReceived(List<ChiTietSkill> receivedSkillList) {
                if (receivedSkillList != null && !receivedSkillList.isEmpty()) {
                    chiTietSkillList.clear(); // Clear existing data
                    for (ChiTietSkill skill : receivedSkillList) {
                        String mask = skill.getMask();
                        firebaseconnet.getSkillName(mask, new firebaseconnet.OnSkillNameReceivedListener() {
                            @Override
                            public void onSkillNameReceived(String skillName) {
                                skill.setMask(skillName);
                                chiTietSkillList.add(skill); // Add updated skill to list
                                Skillnvdapter.notifyDataSetChanged(); // Notify adapter of changes
                            }

                            @Override
                            public void onSkillNameError(Exception e) {
                                Log.e("EmployeeDetail", "Error retrieving skill name", e);
                                skill.setMask("No skill name"); // Default text on error
                                chiTietSkillList.add(skill);
                                Skillnvdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Log.d("EmployeeDetail", "No skills found for employee");
                }
            }

            @Override
            public void onSkillNVListError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving skills", e);
            }
        });
    }

}
