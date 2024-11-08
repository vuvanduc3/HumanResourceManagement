package com.example.humanresourcemanagement.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.databinding.ActivityChucVuDetailBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChucVu;

public class ChucVuDetailActivity extends AppCompatActivity {

    private ActivityChucVuDetailBinding binding;
    private firebaseconnet firebaseConnection;
    private static final String TAG = "ChucVuDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng ViewBinding
        binding = ActivityChucVuDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase connection
        firebaseConnection = new firebaseconnet(this);

        // Lấy chucvu_id từ Intent
        String chucvuId = getIntent().getStringExtra("chucvu_id");
        if (chucvuId != null) {
            loadChucVuDetails(chucvuId);
        } else {
            Log.e(TAG, "No chucvu_id found in intent.");
        }

        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        binding.btnEditCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEditing = binding.tvHSCSDT.isEnabled();
                // Đổi trạng thái của EditText và các nút
                binding.tvHSCSDT.setEnabled(!isEditing);
                binding.btnDeleteCV.setVisibility(isEditing ? View.VISIBLE : View.GONE);
                binding.btnSaveCV.setVisibility(isEditing ? View.GONE : View.VISIBLE);
            }
        });

        // Xử lý khi nhấn nút Lưu
        binding.btnSaveCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lưu dữ liệu và chuyển trạng thái
                saveChucVuDetails();
                binding.tvHSCSDT.setEnabled(false);
                binding.btnDeleteCV.setVisibility(View.VISIBLE);
                binding.btnSaveCV.setVisibility(View.GONE);
            }
        });
        binding.btnDeleteCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị hộp thoại xác nhận
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa chức vụ này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Lấy chucvuId từ Intent hoặc một nguồn nào đó
                            String chucvuId = getIntent().getStringExtra("chucvu_id");

                            firebaseConnection.deleteChucVu(chucvuId, new firebaseconnet.OnChucVuDeleteListener() {
                                @Override
                                public void onChucVuDeleted() {
                                    // Hiển thị thông báo khi xóa thành công
                                    Toast.makeText(getApplicationContext(), "Chức vụ đã được xóa thành công", Toast.LENGTH_SHORT).show();
                                    // Cập nhật giao diện nếu cần, ví dụ như quay lại màn hình trước
                                    finish(); // Hoặc điều hướng tới một màn hình khác
                                }

                                @Override
                                public void onChucVuDeleteError(Exception e) {
                                    // Hiển thị thông báo khi xảy ra lỗi
                                    Toast.makeText(getApplicationContext(), "Lỗi khi xóa chức vụ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> {
                            // Đóng hộp thoại khi người dùng chọn Hủy
                            dialog.dismiss();
                        })
                        .show();
            }
        });


    }

    private void loadChucVuDetails(String chucvuId) {
        firebaseConnection.getChucVuById(chucvuId, new firebaseconnet.OnChucVuReceivedListener() {
            @Override
            public void onChucVuReceived(ChucVu chucVu) {
                // Hiển thị thông tin chức vụ lên các TextView qua ViewBinding
                binding.tvMaChucVuDT.setText(chucVu.getChucvu_id());
                binding.tvHSCSDT.setText(chucVu.getHschucvu());
                binding.tvTenCVDT.setText(chucVu.getLoaichucvu());
            }

            @Override
            public void onChucVuError(Exception e) {
                Log.e(TAG, "Error fetching ChucVu details: ", e);
            }
        });
    }

    private void saveChucVuDetails() {
        // Thêm logic lưu chi tiết chức vụ vào Firebase
        String updatedHscv = binding.tvHSCSDT.getText().toString();
        Log.d(TAG, "Saving updated Hệ số chức vụ: " + updatedHscv);
        // Ví dụ: firebaseConnection.updateHscv(chucvuId, updatedHscv);
        String newHeSoChucVu = binding.tvHSCSDT.getText().toString();
        String chucvuId = getIntent().getStringExtra("chucvu_id");

        // Gọi phương thức editChucVu để cập nhật hệ số chức vụ
        firebaseConnection.editChucVu(chucvuId, newHeSoChucVu, new firebaseconnet.OnChucVuReceivedListener() {
            @Override
            public void onChucVuReceived(ChucVu updatedChucVu) {
                Log.d(TAG, "Updated Chuc Vu successfully.");
                // Cập nhật giao diện sau khi lưu thành công
                binding.tvHSCSDT.setEnabled(false);
                binding.btnDeleteCV.setVisibility(View.VISIBLE);
                binding.btnSaveCV.setVisibility(View.GONE);

                Toast.makeText(ChucVuDetailActivity.this, "Lưu thông tin chức vụ thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChucVuError(Exception e) {
                Log.e(TAG, "Error updating Chuc Vu", e);
                Toast.makeText(ChucVuDetailActivity.this, "Có lỗi xảy ra khi lưu thông tin", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng binding khi không còn dùng
        binding = null;
    }
}
