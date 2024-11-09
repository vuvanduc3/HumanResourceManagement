// Thêm imports cần thiết
package com.example.humanresourcemanagement.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.firebase.bangcapconnet;
import com.example.humanresourcemanagement.model.BangCap;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddBangCapNvActivity extends AppCompatActivity {

    private static final String TAG = "AddBangCapActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private firebaseconnet fbConnect;
    private bangcapconnet bangCapConnect;

    private Spinner spinnerBangCap;
    private EditText edtNgayCap, edtMota;
    private ImageView imgBangCap;
    private Button btnUpload;
    private ProgressDialog progressDialog;

    private Uri imageUri;
    private String imageUrl = "";
    private List<BangCap> bangCapList;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangcapnv_add);

        // Khởi tạo Firebase
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        fbConnect = new firebaseconnet(this);
        bangCapConnect = new bangcapconnet(this);

        // Khởi tạo các view
        initializeViews();
        setupSpinner();
        setupListeners();
    }

    private void initializeViews() {
        spinnerBangCap = findViewById(R.id.spinnerBangCap);
        edtNgayCap = findViewById(R.id.edtNgayCap);
        edtMota = findViewById(R.id.edtMota);
        imgBangCap = findViewById(R.id.imageUrl);
        btnUpload = findViewById(R.id.btnSelectImage);

        // Khởi tạo ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
    }

    private void setupSpinner() {
        // Khởi tạo adapter cho spinner
        bangCapList = new ArrayList<>();
        List<String> bangCapNames = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bangCapNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBangCap.setAdapter(spinnerAdapter);

        // Lấy danh sách bằng cấp từ Firebase
        bangCapConnect.getBangCapList(new bangcapconnet.OnBangCapListReceivedListener() {
            @Override
            public void onBangCapListReceived(List<BangCap> bangCaps) {
                bangCapList.clear();
                bangCapList.addAll(bangCaps);

                // Cập nhật tên bằng cấp vào spinner
                bangCapNames.clear();
                for (BangCap bangCap : bangCaps) {
                    bangCapNames.add(bangCap.getTenBang());
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBangCapListError(Exception e) {
                Toast.makeText(AddBangCapNvActivity.this,
                        "Lỗi khi tải danh sách bằng cấp: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        // Xử lý sự kiện chọn ảnh
        btnUpload.setOnClickListener(v -> openImageChooser());

        // Xử lý sự kiện lưu bằng cấp
        findViewById(R.id.btnSave).setOnClickListener(v -> validateAndSaveBangCap());

        // Xử lý sự kiện quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Xử lý chọn ngày
        edtNgayCap.setOnClickListener(v -> showDatePickerDialog());
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");  // Sửa lại từ "bangcap/*" thành "image/*"
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Hiển thị ảnh đã chọn lên ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgBangCap.setImageBitmap(bitmap);

                // Hiển thị ảnh và ẩn nút upload
                imgBangCap.setVisibility(View.VISIBLE);
                btnUpload.setText("Đổi ảnh khác"); // Tùy chọn: đổi text của button
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi hiển thị ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    edtNgayCap.setText(date);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateAndSaveBangCap() {
        if (spinnerBangCap.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Vui lòng chọn bằng cấp", Toast.LENGTH_SHORT).show();
            return;
        }

        String ngayCap = edtNgayCap.getText().toString().trim();
        String mota = edtMota.getText().toString().trim();

        if (ngayCap.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày cấp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh bằng cấp", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        uploadImageAndSaveData(ngayCap, mota);
    }

    private void uploadImageAndSaveData(String ngayCap, String mota) {
        String randomName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("bangcap/" + randomName + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                imageUrl = uri.toString();
                                saveBangCapInfo(ngayCap, mota, imageUrl);
                            })
                            .addOnFailureListener(e -> handleError("Lỗi khi lấy URL ảnh", e));
                })
                .addOnFailureListener(e -> handleError("Lỗi khi upload ảnh", e))
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred())
                            / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Đang upload: " + (int) progress + "%");
                });
    }

    private void saveBangCapInfo(String ngayCap, String mota, String imageUrl) {
        BangCap selectedBangCap = bangCapList.get(spinnerBangCap.getSelectedItemPosition());
        String employeeId = getIntent().getStringExtra("employeeId");

        if (employeeId == null || employeeId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã nhân viên", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm addBangCapNhanVien từ firebaseconnet
        fbConnect.addBangCapNhanVien(
                employeeId,  // mã nhân viên
                selectedBangCap.getBangcap_id(), // mã bằng cấp
                imageUrl, // URL hình ảnh đã upload
                ngayCap, // ngày cấp bằng
                mota, // nơi cấp/mô tả
                new firebaseconnet.OnBangCapAddListener() {
                    @Override
                    public void onBangCapAdded() {
                        progressDialog.dismiss();
                        Toast.makeText(AddBangCapNvActivity.this,
                                "Thêm bằng cấp thành công", Toast.LENGTH_SHORT).show();

                        // Tạo Intent để quay về EmployeeDetailActivity
                        Intent intent = new Intent();
                        intent.putExtra("shouldRefresh", true); // Thêm flag để báo cần refresh
                        setResult(RESULT_OK, intent);
                        finish();
                    }


                    @Override
                    public void onBangCapAddError(Exception e) {
                        handleError("Lỗi khi lưu thông tin bằng cấp", e);
                    }
                }
        );
    }


    private void handleError(String message, Exception e) {
        progressDialog.dismiss();
        Log.e(TAG, message, e);
        Toast.makeText(this, message + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}