package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.firebase.CCCDFireBase;
import com.example.humanresourcemanagement.databinding.ActivityCccdactivityBinding;
import com.example.humanresourcemanagement.model.CCCD;
import com.squareup.picasso.Picasso;

public class CCCDActivity extends AppCompatActivity {

    private ActivityCccdactivityBinding binding;
    private CCCDFireBase cccdFireBase;
    private Uri backImageUri, frontImageUri;
    private ActivityResultLauncher<Intent> selectImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Liên kết với view binding
        binding = ActivityCccdactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        cccdFireBase = new CCCDFireBase(this);
        loadDataCCCD("123456789");
        // Đăng ký ActivityResultLauncher để chọn ảnh
        selectImageBack = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        backImageUri = result.getData().getData();
                        Picasso.get().load(backImageUri).into(binding.backCccdImage);
                    }
                });

        // Lắng nghe sự kiện click để chọn ảnh mặt sau
        binding.backCccdImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectImageBack.launch(intent);
        });

        // Lắng nghe sự kiện click để chọn ảnh mặt trước
        binding.frontCccdImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectImageBack.launch(intent);
        });

        // Cập nhật hoặc lưu ảnh
        binding.btnLuu.setOnClickListener(v -> {

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
