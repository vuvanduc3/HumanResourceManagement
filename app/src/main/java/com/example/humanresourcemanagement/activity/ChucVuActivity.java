package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.ChucVuAdapter;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChucVu;

import java.util.ArrayList;
import java.util.List;

public class ChucVuActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChucVu;
    private ChucVuAdapter adapterChucVu;
    private List<ChucVu> chucVuList;
    private firebaseconnet firebaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuc_vu);

        // Khởi tạo Firebase connection
        firebaseConnection = new firebaseconnet(this);

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
    }

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
                Log.e("ChucVuActivity", "Error getting chucvu list: ", e);
            }
        });
    }
}
