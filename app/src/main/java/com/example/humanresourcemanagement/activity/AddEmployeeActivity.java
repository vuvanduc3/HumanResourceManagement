package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.databinding.ActivityAddEmployeeBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddEmployeeActivity extends AppCompatActivity {

    private ActivityAddEmployeeBinding binding;
    private firebaseconnet firebaseconnet;
    private ArrayList<String> departmentList = new ArrayList<>();
    private ArrayList<String> positionList = new ArrayList<>();
    private ArrayAdapter<String> departmentAdapter;
    private ArrayAdapter<String> positionAdapter;
    private ArrayAdapter<String> genderAdapter;
    private ArrayList<String> genderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        firebaseconnet = new firebaseconnet(this);

        // Khởi tạo adapter cho Spinner Department
        departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentList);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDepartment.setAdapter(departmentAdapter);

        // Khởi tạo adapter cho Spinner Position
        positionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, positionList);
        positionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPosition.setAdapter(positionAdapter);

        // Khởi tạo adapter cho Spinner Gender
        genderList.add("Nam");
        genderList.add("Nữ");
        genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGender.setAdapter(genderAdapter);

        // Lấy dữ liệu từ Firebase
        getDepartmentData();
        getPositionData();

        // Lắng nghe sự kiện khi nhấn nút Lưu
        binding.btnThem.setOnClickListener(view -> addEmployee());

        // Lắng nghe sự kiện chọn item trong Spinner Department
        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDepartment = departmentList.get(position);
                Log.d("AddEmployeeActivity", "Department selected: " + selectedDepartment);
                Toast.makeText(AddEmployeeActivity.this, "Selected: " + selectedDepartment, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có item nào được chọn
            }
        });

        // Lắng nghe sự kiện chọn item trong Spinner Position
        binding.spinnerPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedPosition = positionList.get(position);
                Log.d("AddEmployeeActivity", "Position selected: " + selectedPosition);
                Toast.makeText(AddEmployeeActivity.this, "Selected: " + selectedPosition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có item nào được chọn
            }
        });

        // Lắng nghe sự kiện chọn item trong Spinner Gender
        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedGender = genderList.get(position);
                Log.d("AddEmployeeActivity", "Gender selected: " + selectedGender);
                Toast.makeText(AddEmployeeActivity.this, "Selected: " + selectedGender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có item nào được chọn
            }
        });
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
                        Log.d("AddEmployeeActivity", "Department data updated");
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
                        Log.d("AddEmployeeActivity", "Position data updated");
                    }
                })
                .addOnFailureListener(e -> Log.e("AddEmployeeActivity", "Error getting position data", e));
    }

    private void addEmployee() {
        // Lấy thông tin từ các trường nhập liệu
        String name = binding.etName.getText().toString();
        String address = binding.etAddress.getText().toString();
        String phone = binding.etPhone.getText().toString();
        String birthDate = binding.etBirthDate.getText().toString();
        String manv = binding.etEmployeeId.getText().toString();
        String cccd = binding.etCccd.getText().toString();
        String salary = binding.etBasicSalary.getText().toString();
        String gender = binding.spinnerGender.getSelectedItem().toString();
        String department = binding.spinnerDepartment.getSelectedItem().toString();
        String position = binding.spinnerPosition.getSelectedItem().toString();
        String status = "true";

        // Tạo đối tượng Employee mới
        Employee newEmployee = new Employee(cccd, position, address, manv, gender,
                "", salary, manv, name, birthDate, birthDate, department, phone, status);

        // Thêm nhân viên vào Firebase
        firebaseconnet.addEmployee(newEmployee, new firebaseconnet.OnEmployeeAddedListener() {
            @Override
            public void onEmployeeAdded() {
                Toast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onEmployeeAddError(Exception e) {
                Log.e("AddEmployee", "Error adding employee", e);
                Toast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Log.e("AddEmployee", "Error: " + e.getMessage());
                Toast.makeText(AddEmployeeActivity.this, "Có lỗi xảy ra, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
