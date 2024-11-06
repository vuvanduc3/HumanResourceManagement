package com.example.humanresourcemanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChucVu;

public class ChucVuDetailActivity extends AppCompatActivity {

    private TextView tvChucVuId, tvHsChucVu, tvLoaiChucVu;
    private firebaseconnet firebaseConnection;
    private static final String TAG = "ChucVuDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuc_vu_detail);

        // Ánh xạ các TextView
        tvChucVuId = findViewById(R.id.tvMaChucVuDT);
        tvHsChucVu = findViewById(R.id.tvHSCSDT);
        tvLoaiChucVu = findViewById(R.id.tvTenCVDT);

        // Khởi tạo Firebase connection
        firebaseConnection = new firebaseconnet(this);

        // Lấy chucvu_id từ Intent
        String chucvuId = getIntent().getStringExtra("chucvu_id");
        if (chucvuId != null) {
            loadChucVuDetails(chucvuId);
        } else {
            Log.e(TAG, "No chucvu_id found in intent.");
        }
    }

    private void loadChucVuDetails(String chucvuId) {
        firebaseConnection.getChucVuById(chucvuId, new firebaseconnet.OnChucVuReceivedListener() {
            @Override
            public void onChucVuReceived(ChucVu chucVu) {
                // Hiển thị thông tin chức vụ lên các TextView
                tvChucVuId.setText(chucVu.getChucvu_id());
                tvHsChucVu.setText(chucVu.getHschucvu());
                tvLoaiChucVu.setText(chucVu.getLoaichucvu());
            }

            @Override
            public void onChucVuError(Exception e) {
                Log.e(TAG, "Error fetching ChucVu details: ", e);
            }
        });
    }
}
