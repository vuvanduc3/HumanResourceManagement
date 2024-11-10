package com.example.humanresourcemanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityCccdactivityBinding;
import com.example.humanresourcemanagement.firebase.CCCDFireBase;
import com.example.humanresourcemanagement.model.CCCD;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class CCCDActivity extends AppCompatActivity {

    private ActivityCccdactivityBinding binding; // binding view
    private CCCDFireBase cccdFireBase;
    String cccdId;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int imageSide = 0; // 1 = mặt trước, 2 = mặt sau
    private Uri selectedFrontImageUri;
    private Uri selectedBackImageUri;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng ViewBinding
        binding = ActivityCccdactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Khởi tạo Firebase
        cccdFireBase = new CCCDFireBase(this);
        cccdId = getIntent().getStringExtra("cccd");
        loadDataCCCD(cccdId);
        // Khởi tạo ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        // Xử lý sự kiện nhấn vào ảnh để chọn ảnh
        binding.frontCccdImage.setOnClickListener(v -> openImageChooser(1)); // Mặt trước
        binding.backCccdImage.setOnClickListener(v -> openImageChooser(2));  // Mặt sau

        binding.cccdNumber.setText(cccdId);

        // Xử lý nút quay lại
        binding.btnBack.setOnClickListener(v -> finish());

        // Xử lý nút lưu
        binding.btnLuu.setOnClickListener(v -> saveCCCDInfo(storageRef));
    }

    // Mở gallery để chọn ảnh
    private void openImageChooser(int side) {
        imageSide = side;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả chọn ảnh từ gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            if (imageSide == 1) {
                selectedFrontImageUri = imageUri;
                Picasso.get().load(selectedFrontImageUri).into(binding.frontCccdImage); // Hiển thị ảnh mặt trước
            } else if (imageSide == 2) {
                selectedBackImageUri = imageUri;
                Picasso.get().load(selectedBackImageUri).into(binding.backCccdImage); // Hiển thị ảnh mặt sau
            }
        }
    }

    private void saveCCCDInfo(StorageReference storageRef) {
        if (selectedFrontImageUri == null || selectedBackImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh mặt trước và mặt sau CCCD", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị ProgressDialog khi bắt đầu tải ảnh
        progressDialog.show();

        // Lưu ảnh mặt trước lên Firebase Storage
        StorageReference frontImageRef = storageRef.child("cccd/" + cccdId + "/front.jpg");
        frontImageRef.putFile(selectedFrontImageUri)
                .addOnSuccessListener(taskSnapshot -> frontImageRef.getDownloadUrl().addOnSuccessListener(frontUri -> {
                    String frontImageUrl = frontUri.toString();

                    // Lưu ảnh mặt sau lên Firebase Storage
                    StorageReference backImageRef = storageRef.child("cccd/" + cccdId + "/back.jpg");
                    backImageRef.putFile(selectedBackImageUri)
                            .addOnSuccessListener(taskSnapshot1 -> backImageRef.getDownloadUrl().addOnSuccessListener(backUri -> {
                                String backImageUrl = backUri.toString();

                                // Lưu thông tin CCCD vào Firestore
                                saveToDatabase(frontImageUrl, backImageUrl);
                            }))
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi khi tải ảnh mặt sau lên", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss(); // Ẩn ProgressDialog khi có lỗi
                            });
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải ảnh mặt trước lên", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss(); // Ẩn ProgressDialog khi có lỗi
                });
    }

    private void saveToDatabase(String frontImageUrl, String backImageUrl) {
        // Tạo đối tượng CCCD với cả hai mặt ảnh
        CCCD cccd = new CCCD(cccdId, frontImageUrl, backImageUrl);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Lưu vào Firestore
        firestore.collection("cccd").document(cccdId)
                .set(cccd)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Lưu thông tin CCCD thành công", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss(); // Ẩn ProgressDialog khi lưu thành công
                    finish(); // Quay lại hoặc thực hiện thao tác khác sau khi lưu thành công
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi lưu thông tin CCCD", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss(); // Ẩn ProgressDialog khi có lỗi
                });
    }

    private void loadDataCCCD(String cccdId) {

        cccdFireBase.getCCCDData(cccdId, new CCCDFireBase.OnCCCDDataFetchedListener() {
            @Override
            public void onSuccess(CCCD cccd) {

                // Hiển thị hình ảnh sử dụng thư viện Picasso
                if (cccd.getBackImage() != null && !cccd.getBackImage().isEmpty()) {
                    Picasso.get().load(cccd.getBackImage()).into(binding.backCccdImage);
                }
                else{
                    binding.backCccdImage.setImageResource(R.drawable.add_cccd);
                }

                if (cccd.getFrontImage() != null && !cccd.getFrontImage().isEmpty()) {
                    Picasso.get().load(cccd.getFrontImage()).into(binding.frontCccdImage);
                }
                else{
                    binding.frontCccdImage.setImageResource(R.drawable.add_cccd);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


}
