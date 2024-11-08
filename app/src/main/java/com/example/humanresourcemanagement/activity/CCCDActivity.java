package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.firebase.CCCDFireBase;
import com.example.humanresourcemanagement.databinding.ActivityCccdactivityBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.CCCD;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class CCCDActivity extends AppCompatActivity {

    private ActivityCccdactivityBinding binding;
    private CCCDFireBase cccdFireBase;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bật tính năng EdgeToEdge cho giao diện
        EdgeToEdge.enable(this);

        // Liên kết với view binding
        binding = ActivityCccdactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase
        cccdFireBase = new CCCDFireBase(this);
        String cccdId = "123456789"; // Thay thế bằng ID thực tế của CCCD

        loadDataCCCD(cccdId);
        // Lấy dữ liệu từ Firestore


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
