package com.example.humanresourcemanagement.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.ChucVuAdapter;
import com.example.humanresourcemanagement.databinding.ActivityChucVuBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChucVu;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ChucVuActivity extends AppCompatActivity {
    private ActivityChucVuBinding binding;
    private RecyclerView recyclerViewChucVu;
    private ChucVuAdapter adapterChucVu;
    private List<ChucVu> chucVuList;
    private firebaseconnet firebaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo ViewBinding
        binding = ActivityChucVuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase connection
        firebaseConnection = new firebaseconnet(this);
        loadChucVuData();
        // Khởi tạo RecyclerView và Adapter
        recyclerViewChucVu = findViewById(R.id.recyclerViewChucVu);
        recyclerViewChucVu.setLayoutManager(new LinearLayoutManager(this));

        chucVuList = new ArrayList<>();
        adapterChucVu = new ChucVuAdapter(chucVuList);
        recyclerViewChucVu.setAdapter(adapterChucVu);

        // Thiết lập OnItemClickListener để chuyển đến màn hình chi tiết khi nhấn vào item
        adapterChucVu.setOnItemClickListener(chucVu -> {
            Intent intent = new Intent(ChucVuActivity.this, ChucVuDetailActivity.class);
            intent.putExtra("chucvu_id", chucVu.getChucvu_id());
            startActivity(intent);
        });

        // Tải dữ liệu chức vụ từ Firebase
        loadChucVuData();

        binding.tvAddd.setOnClickListener(view -> {
            // Tạo một AlertDialog với TextInputLayout cho các EditText
            AlertDialog.Builder builder = new AlertDialog.Builder(ChucVuActivity.this);
            builder.setTitle("Thêm Chức Vụ");

            // Tạo Layout chứa các trường nhập liệu
            LinearLayout layout = new LinearLayout(ChucVuActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 30, 50, 30); // Thêm padding cho layout

            // Tạo TextInputLayout để bao bọc EditText cho mã chức vụ
            TextInputLayout inputChucVuId = new TextInputLayout(ChucVuActivity.this);
            inputChucVuId.setPadding(10, 0, 10, 0); // Thêm padding cho TextInputLayout
            final EditText etChucVuId = new EditText(ChucVuActivity.this);
            etChucVuId.setHint("Nhập mã chức vụ");
            inputChucVuId.addView(etChucVuId);
            layout.addView(inputChucVuId);

            // Tạo TextInputLayout cho tên chức vụ
            TextInputLayout inputTenChucVu = new TextInputLayout(ChucVuActivity.this);
            inputTenChucVu.setPadding(10, 0, 10, 0);
            final EditText etTenChucVu = new EditText(ChucVuActivity.this);
            etTenChucVu.setHint("Nhập tên chức vụ");
            inputTenChucVu.addView(etTenChucVu);
            layout.addView(inputTenChucVu);

            // Tạo TextInputLayout cho hệ số chức vụ
            TextInputLayout inputHeSoChucVu = new TextInputLayout(ChucVuActivity.this);
            inputHeSoChucVu.setPadding(10, 0, 10, 0);
            final EditText etHeSoChucVu = new EditText(ChucVuActivity.this);
            etHeSoChucVu.setHint("Nhập hệ số chức vụ");
            inputHeSoChucVu.addView(etHeSoChucVu);
            layout.addView(inputHeSoChucVu);

            builder.setView(layout);

            // Thêm nút "Thêm" và "Hủy"
            builder.setPositiveButton("Thêm", (dialog, which) -> {
                // Lấy giá trị từ EditText
                String chucvuId = etChucVuId.getText().toString().trim();
                String tenChucVu = etTenChucVu.getText().toString().trim();
                String heSoChucVu = etHeSoChucVu.getText().toString().trim();

                // Kiểm tra dữ liệu nhập
                if (!chucvuId.isEmpty() && !tenChucVu.isEmpty() && !heSoChucVu.isEmpty()) {
                    // Gọi hàm addChucVu với các tham số
                    addChucVu(chucvuId, tenChucVu, heSoChucVu, new firebaseconnet.OnChucVuAddListener() {
                        @Override
                        public void onChucVuAdded() {
                            // Xử lý khi thêm thành công
                            Toast.makeText(ChucVuActivity.this, "Chức vụ đã được thêm!", Toast.LENGTH_SHORT).show();
                            loadChucVuData(); // Tải lại danh sách
                        }

                        @Override
                        public void onChucVuAddError(Exception e) {
                            // Xử lý khi có lỗi
                            Toast.makeText(ChucVuActivity.this, "Lỗi khi thêm chức vụ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Hiển thị thông báo nếu dữ liệu không hợp lệ
                    Toast.makeText(ChucVuActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss()); // Đóng dialog khi bấm "Hủy"

            // Tạo AlertDialog đẹp hơn
            builder.create().show();
        });

    }

    // Phương thức gọi Firebase để thêm chức vụ
    private void addChucVu(String chucvuId, String tenChucVu, String heSoChucVu, firebaseconnet.OnChucVuAddListener listener) {
        firebaseConnection.addChucVu(chucvuId, tenChucVu, heSoChucVu, new firebaseconnet.OnChucVuAddListener() {
            @Override
            public void onChucVuAdded() {
                listener.onChucVuAdded(); // Gọi callback khi thêm thành công
            }

            @Override
            public void onChucVuAddError(Exception e) {
                listener.onChucVuAddError(e); // Gọi callback khi có lỗi
            }
        });
    }

    // Tải danh sách chức vụ từ Firebase
    private void loadChucVuData() {
        firebaseConnection.getChucVuList(new firebaseconnet.OnChucVuListReceivedListener() {
            @Override
            public void onChucVuListReceived(List<ChucVu> fetchedChucVuList) {
                chucVuList.clear();
                chucVuList.addAll(fetchedChucVuList);
                adapterChucVu.notifyDataSetChanged(); // Thông báo Adapter để cập nhật RecyclerView
            }

            @Override
            public void onChucVuListError(Exception e) {
                // Xử lý lỗi khi lấy danh sách chức vụ
            }
        });
    }
}
