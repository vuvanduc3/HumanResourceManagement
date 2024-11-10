package com.example.humanresourcemanagement.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.humanresourcemanagement.firebase.firebaseconnet;

import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.humanresourcemanagement.databinding.ActivityEmployeeEditBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class EmployeeEditActivity extends AppCompatActivity {
    private firebaseconnet firebaseconnet;
    private ActivityEmployeeEditBinding binding;
    private Uri selectedImageUri = null;
    private ArrayAdapter<String> departmentAdapter;
    private ArrayAdapter<String> positionAdapter;
    private ArrayAdapter<String> genderAdapter;
    private ArrayList<String> genderList = new ArrayList<>();
    private ArrayList<String> departmentList = new ArrayList<>();
    private ArrayAdapter<String> trangthaiAdapter;
    private ArrayList<String> trangthaiList = new ArrayList<>();
    private ArrayList<String> positionList = new ArrayList<>();
    private final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    // Khởi tạo ActivityResultLauncher để chọn ảnh từ thư viện
    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    binding.profileImageView.setImageURI(selectedImageUri);  // Hiển thị ảnh mới đã chọn
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);
// Lấy employeeId từ Intent
        String employeeId = getIntent().getStringExtra("employeeId");
        // Xử lý sự kiện chọn ảnh
        binding.buttonSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            selectImageLauncher.launch(intent);
        });
// Gọi hàm lấy thông tin nhân viên
        getEmployeeDetails(employeeId);
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        // Xử lý sự kiện cập nhật thông tin nhân viên
        binding.buttonSave.setOnClickListener(v -> {

            updateEmployeeDetails(employeeId);
        });
        // Khởi tạo adapter cho Spinner Department
        departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentList);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.editTextPhongBanId.setAdapter(departmentAdapter);

        // Khởi tạo adapter cho Spinner Position
        positionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, positionList);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.editTextChucVuId.setAdapter(positionAdapter);

        // Khởi tạo adapter cho Spinner Gender
        genderList.add("Nam");
        genderList.add("Nữ");
        genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.editTextGioiTinh.setAdapter(genderAdapter);
        // Khởi tạo adapter cho Spinner Gender
        trangthaiList.add("true");
        trangthaiList.add("false");
        trangthaiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangthaiList);
        trangthaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.editTextTrangThai.setAdapter(trangthaiAdapter);

        // Lấy dữ liệu từ Firebase
        getDepartmentData();
        getPositionData();

        // Lắng nghe sự kiện chọn item trong Spinner Department
        binding.editTextPhongBanId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDepartment = departmentList.get(position);
                Log.d("AddEmployeeActivity", "Department selected: " + selectedDepartment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có item nào được chọn
            }
        });

        // Lắng nghe sự kiện chọn item trong Spinner Position
        binding.editTextChucVuId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedPosition = positionList.get(position);
                Log.d("AddEmployeeActivity", "Position selected: " + selectedPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có item nào được chọn
            }
        });

        // Lắng nghe sự kiện chọn item trong Spinner Gender
        binding.editTextGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedGender = genderList.get(position);
                Log.d("AddEmployeeActivity", "Gender selected: " + selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có item nào được chọn
            }
        });


        // Sự kiện nhấn vào EditText etBirthDate để chọn ngày sinh
        binding.editTextNgaySinh.setOnClickListener(view -> showDatePickerDialog(binding.editTextNgaySinh));

        // Sự kiện nhấn vào EditText etStartDate để chọn ngày bắt đầu
        binding.editTextNgayBatDau.setOnClickListener(view -> showDatePickerDialog(binding.editTextNgayBatDau));
    }
    private void getEmployeeDetails(String employeeId) {
        firebaseconnet.getEmployeeById(employeeId, new firebaseconnet.OnEmployeeReceivedListener() {
            @Override
            public void onEmployeeReceived(Employee employee) {
                // Cập nhật UI với thông tin nhân viên
                binding.editTextName.setText(employee.getName());
                // Giả sử bạn đã có spinnerPB (Spinner) và departmentList (danh sách phòng ban)
                String chucvuId = employee.getChucvuId();

// Tìm chỉ số của chucvuId trong departmentList
                int position = departmentList.indexOf(chucvuId);

                if (position != -1) {
                    // Nếu tìm thấy, chọn mục trong Spinner
                    binding.editTextChucVuId.setSelection(position);
                } else {
                    // Nếu không tìm thấy, có thể chọn một giá trị mặc định (ví dụ: vị trí 0)
                    binding.editTextChucVuId.setSelection(0);
                }

                // Giả sử bạn đã có spinnerPB (Spinner) và departmentList (danh sách phòng ban)
                String phongBanId = employee.getPhongbanId();

// Tìm chỉ số của chucvuId trong departmentList
                int position1 = positionList.indexOf(phongBanId);

                if (position1 != -1) {
                    // Nếu tìm thấy, chọn mục trong Spinner
                    binding.editTextPhongBanId.setSelection(position1);
                } else {
                    // Nếu không tìm thấy, có thể chọn một giá trị mặc định (ví dụ: vị trí 0)
                    binding.editTextPhongBanId.setSelection(0);
                }

                // Giả sử bạn đã có spinnerPB (Spinner) và departmentList (danh sách phòng ban)
                String gt = employee.getGioitinh();

// Tìm chỉ số của chucvuId trong departmentList
                int position2 = genderList.indexOf(gt);

                if (position2 != -1) {
                    // Nếu tìm thấy, chọn mục trong Spinner
                    binding.editTextGioiTinh.setSelection(position2);
                } else {
                    // Nếu không tìm thấy, có thể chọn một giá trị mặc định (ví dụ: vị trí 0)
                    binding.editTextPhongBanId.setSelection(0);
                }

                // Giả sử bạn đã có spinnerPB (Spinner) và departmentList (danh sách phòng ban)
                String tt = employee.getTrangthai();

// Tìm chỉ số của chucvuId trong departmentList
                int position3 = trangthaiList.indexOf(gt);

                if (position3 != -1) {
                    // Nếu tìm thấy, chọn mục trong Spinner
                    binding.editTextTrangThai.setSelection(position3);
                } else {
                    // Nếu không tìm thấy, có thể chọn một giá trị mặc định (ví dụ: vị trí 0)
                    binding.editTextTrangThai.setSelection(0);
                }

                binding.editTextDiaChi.setText(employee.getDiachi());
                binding.editTextSDT.setText(employee.getSdt());
                binding.editTextNgaySinh.setText(employee.getNgaysinh());
                binding.editTextNgayBatDau.setText(employee.getNgaybatdau());

                binding.editTextCCCD.setText(employee.getCccd());
                binding.editTextLuongCoBan.setText(employee.getLuongcoban());
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

    // Hàm mở DatePickerDialog cho các trường ngày tháng
    private void showDatePickerDialog(EditText editText) {
        // Lấy ngày hiện tại làm mặc định cho DatePicker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo và hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Định dạng ngày thành "dd/MM/yyyy" và đặt vào EditText
            String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
            editText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
    private void getDepartmentData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference departmentRef = db.collection("phongban");

        departmentRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        departmentList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String departmentName = document.getString("maPhongBan");
                            departmentList.add(departmentName);
                        }
                        departmentAdapter.notifyDataSetChanged();  // Đảm bảo gọi notifyDataSetChanged
                    }
                })
                .addOnFailureListener(e -> Log.e("AddEmployeeActivity", "Error getting department data", e));
    }

    private void getPositionData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference positionRef = db.collection("chucvu");

        positionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        positionList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String positionName = document.getString("chucvu_id");
                            positionList.add(positionName);
                        }
                        positionAdapter.notifyDataSetChanged();  // Đảm bảo gọi notifyDataSetChanged
                    }
                })
                .addOnFailureListener(e -> Log.e("AddEmployeeActivity", "Error getting position data", e));
    }
    private void updateEmployeeDetails(String employeeId) {
        // Thu thập dữ liệu từ các trường nhập liệu
        String name = binding.editTextName.getText().toString();
        String position = binding.editTextChucVuId.getSelectedItem().toString();
        String address = binding.editTextDiaChi.getText().toString();
        String phone = binding.editTextSDT.getText().toString();
        String birthDate = binding.editTextNgaySinh.getText().toString();
        String status = binding.editTextTrangThai.getSelectedItem().toString();
        String cccd = binding.editTextCCCD.getText().toString();
        String salary = binding.editTextLuongCoBan.getText().toString();
        String gender = binding.editTextGioiTinh.getSelectedItem().toString();
        String pb = binding.editTextPhongBanId.getSelectedItem().toString();

        Employee updatedEmployee = new Employee(cccd, position, address, employeeId, gender,
                employeeId, salary, "", name, birthDate, birthDate, pb, phone, status);

        if (selectedImageUri != null) {
            // Tải ảnh lên Firebase Storage nếu người dùng chọn ảnh mới
            StorageReference imageRef = storageRef.child("images/" + employeeId + ".jpg");
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        updatedEmployee.setImageUrl(uri.toString()); // Cập nhật link hình ảnh
                        // Sau khi có link, cập nhật thông tin nhân viên
                        updateEmployeeInFirestore(employeeId, updatedEmployee);
                    }))
                    .addOnFailureListener(e -> {
                        Log.e("EmployeeEdit", "Error uploading image", e);
                        Toast.makeText(this, "Lỗi tải ảnh", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Nếu không có ảnh mới, chỉ cập nhật các trường khác
            updateEmployeeInFirestore(employeeId, updatedEmployee);
        }
    }

    // Phương thức cập nhật dữ liệu nhân viên trong Firestore
    private void updateEmployeeInFirestore(String employeeId, Employee updatedEmployee) {
        firebaseconnet.updateEmployee(employeeId, updatedEmployee, new firebaseconnet.OnEmployeeUpdatedListener() {
            @Override
            public void onEmployeeUpdated() {
                Toast.makeText(EmployeeEditActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Exception e) {
                Log.e("EmployeeEdit", "Error updating employee details", e);
                Toast.makeText(EmployeeEditActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
