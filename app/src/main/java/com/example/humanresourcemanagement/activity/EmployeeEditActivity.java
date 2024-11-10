package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeEditBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class EmployeeEditActivity extends AppCompatActivity {

    private ActivityEmployeeEditBinding binding;
    private firebaseconnet firebaseconnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy employeeId từ Intent
        String employeeId = getIntent().getStringExtra("employeeId");

        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);

        // Gọi hàm lấy thông tin nhân viên
        getEmployeeDetails(employeeId);
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        // Lắng nghe sự kiện khi người dùng nhấn nút Lưu
        binding.buttonSave.setOnClickListener(view -> {
            // Gọi hàm cập nhật thông tin nhân viên
            updateEmployeeDetails(employeeId);
        });
    }

    private void getEmployeeDetails(String employeeId) {
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                // Cập nhật UI với thông tin nhân viên
                binding.editTextName.setText(employee.getName());
                binding.editTextChucVuId.setText(employee.getChucvuId());
                binding.editTextDiaChi.setText(employee.getDiachi());
                binding.editTextSDT.setText(employee.getSdt());
                binding.editTextNgaySinh.setText(employee.getNgaysinh());
                binding.editTextNgayBatDau.setText(employee.getNgaybatdau());
                binding.editTextTrangThai.setText(employee.getTrangthai());
                binding.editTextCCCD.setText(employee.getCccd());
                binding.editTextPhongBanId.setText(employee.getPhongbanId());
                binding.editTextLuongCoBan.setText(employee.getLuongcoban());
                binding.editTextGioiTinh.setText(employee.getGioitinh());
                binding.editTextEmployeeId.setText(employee.getEmployeeId());

                // Hiển thị hình ảnh sử dụng thư viện Picasso
                if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
                    Picasso.get().load(employee.getImageUrl()).into(binding.profileImageView);
                }
            }

            @Override
            public void onEmployeeError(Exception e) {
                Log.e("EmployeeEdit", "Error retrieving employee details", e);
            }
        });
    }
    private void updateEmployeeDetails(String employeeId) {
        // Lấy thông tin từ các trường chỉnh sửa
        String name = binding.editTextName.getText().toString();
        String position = binding.editTextChucVuId.getText().toString();
        String address = binding.editTextDiaChi.getText().toString();
        String phone = binding.editTextSDT.getText().toString();
        String birthDate = binding.editTextNgaySinh.getText().toString();
        String status = binding.editTextTrangThai.getText().toString();
        String cccd = binding.editTextCCCD.getText().toString();
        String salary = binding.editTextLuongCoBan.getText().toString();
        String gender = binding.editTextGioiTinh.getText().toString();
        String pb = binding.editTextPhongBanId.getText().toString();

        // Lấy thông tin nhân viên hiện tại để so sánh
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee currentEmployee) {
                // Tạo đối tượng Employee mới với thông tin chỉnh sửa
                Employee updatedEmployee = new Employee(cccd, position, address, employeeId, gender,
                        employeeId, salary, "", name,
                        birthDate, birthDate, pb , phone, status);

                // Kiểm tra chức vụ và thực hiện cập nhật tương ứng
                if (currentEmployee.getChucvuId().equals("TP") && !position.equals("TP")) {
                    // Nếu chức vụ hiện tại là "TP" và đang chuyển xuống "NV"
                    updateDepartmentHead(pb, ""); // Cập nhật maPhongBan
                } else if (!currentEmployee.getChucvuId().equals("TP") && position.equals("TP")) {
                    // Nếu chức vụ hiện tại không phải là "TP" và đang chuyển lên "TP"
                    updateDepartmentHead(pb, employeeId); // Cập nhật trưởng phòng mới
                }

                // Gọi hàm cập nhật dữ liệu vào Firebase
                firebaseconnet.updateEmployee(employeeId, updatedEmployee, new firebaseconnet.OnEmployeeUpdatedListener() {
                    @Override
                    public void onEmployeeUpdated() {
                        Toast.makeText(EmployeeEditActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish(); // Kết thúc activity sau khi cập nhật thành công
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("EmployeeEdit", "Error updating employee details", e);
                        Toast.makeText(EmployeeEditActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onEmployeeError(Exception e) {
                Log.e("EmployeeEdit", "Error retrieving current employee details", e);
                Toast.makeText(EmployeeEditActivity.this, "Lỗi khi lấy thông tin nhân viên hiện tại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateDepartmentHead(String pb, String newHeadId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (newHeadId.equals("")) {
            // Trường hợp trưởng phòng hiện tại bị hạ xuống NV

            db.collection("employees")
                    .whereEqualTo("phongbanId", pb)
                    .whereEqualTo("chucvuId", "TP")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        // Cập nhật maQuanLy phòng ban thành "" vì không còn trưởng phòng
                        db.collection("phongban").document(pb)
                                .update("maQuanLy", "")
                                .addOnSuccessListener(aVoid1 ->
                                        Log.d("EmployeeEdit", "Cleared maQuanLy for department to empty")
                                )
                                .addOnFailureListener(e ->
                                        Log.e("EmployeeEdit", "Error updating department maQuanLy", e)
                                );


                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                String oldHeadId = document.getId(); // Lưu ID của trưởng phòng cũ


                                // Cập nhật chức vụ của trưởng phòng cũ thành NV
                                db.collection("employees").document(oldHeadId)
                                        .update("chucvuId", "NV")
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("EmployeeEdit", "Updated old head to NV");

                                        })
                                        .addOnFailureListener(e -> Log.e("EmployeeEdit", "Error updating old head position", e));
                            }
                        } else {
                            Log.w("EmployeeEdit", "No department head found to update.");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("EmployeeEdit", "Error getting old head data", e));
        } else {
            // Trường hợp khi một trưởng phòng mới được bổ nhiệm
            // Cập nhật chức vụ cho trưởng phòng mới
            db.collection("employees")
                    .document(newHeadId)
                    .update("chucvuId", "TP", "phongbanId", pb) // Cập nhật phongbanId nếu cần
                    .addOnSuccessListener(aVoid -> {
                        Log.d("EmployeeEdit", "Updated new head to TP");

                        // Cập nhật maQuanLy của phòng ban để phản ánh trưởng phòng mới
                        db.collection("phongban").document(pb)
                                .update("maQuanLy", newHeadId) // Cập nhật maQuanLy với ID của trưởng phòng mới
                                .addOnSuccessListener(aVoid1 ->
                                        Log.d("EmployeeEdit", "Updated maQuanLy for department to new head ID")
                                )
                                .addOnFailureListener(e ->
                                        Log.e("EmployeeEdit", "Error updating department maQuanLy", e)
                                );
                    })
                    .addOnFailureListener(e -> Log.e("EmployeeEdit", "Error updating new head position", e));
        }
    }
}
