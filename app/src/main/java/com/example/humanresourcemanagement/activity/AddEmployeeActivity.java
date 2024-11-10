package com.example.humanresourcemanagement.activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.databinding.ActivityAddEmployeeBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddEmployeeActivity extends AppCompatActivity {

    private ActivityAddEmployeeBinding binding;
    private firebaseconnet firebaseconnet;
    private ArrayList<String> departmentList = new ArrayList<>();
    private ArrayList<String> positionList = new ArrayList<>();
    private ArrayAdapter<String> departmentAdapter;
    private ArrayAdapter<String> positionAdapter;
    private ArrayAdapter<String> genderAdapter;
    private ArrayList<String> genderList = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Xử lý khi không có item nào được chọn
            }
        });

        // Lắng nghe sự kiện chọn hình ảnh
        binding.btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 openImageChooser();
            }
        });
        // Sự kiện nhấn vào EditText etBirthDate để chọn ngày sinh
        binding.etBirthDate.setOnClickListener(view -> showDatePickerDialog(binding.etBirthDate));

        // Sự kiện nhấn vào EditText etStartDate để chọn ngày bắt đầu
        binding.etStartDate.setOnClickListener(view -> showDatePickerDialog(binding.etStartDate));

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
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.imgProfile.setImageBitmap(bitmap);  // Hiển thị hình ảnh lên ImageView
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        String imgUrl = imageUri.toString();

        // Tạo đối tượng Employee mới
        Employee newEmployee = new Employee(cccd, position, address, manv, gender,
                "",imgUrl, salary, manv, name, birthDate, birthDate, department, phone, status);

        // Thêm nhân viên vào Firebase
        firebaseconnet.addNhanVien2(newEmployee, new firebaseconnet.OnEmployeeAddedListener() {
            @Override
            public void onEmployeeAdded() {
                Toast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onEmployeeAddError(Exception e) {
                Toast.makeText(AddEmployeeActivity.this, "Lỗi khi thêm nhân viên", Toast.LENGTH_SHORT).show();
                Log.e("AddEmployeeActivity", "Error adding employee", e);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
