package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.ThongBaoAdapter;
import com.example.humanresourcemanagement.model.ThongBao;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificeScreen extends AppCompatActivity {

    private ListView listView;
    private ThongBaoAdapter adapter;
    private List<ThongBao> thongBaoList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifice_screen);

        listView = findViewById(R.id.listViewNotifications);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        thongBaoList = new ArrayList<>();
        adapter = new ThongBaoAdapter(this, thongBaoList);
        listView.setAdapter(adapter);

        // Thêm 3 thông báo tĩnh
        addStaticData();

        // Lấy danh sách thông báo từ Firebase
        loadThongBaoFromFirebase();

    }

    // Phương thức để lấy danh sách thông báo từ Firebase
    private void loadThongBaoFromFirebase() {
        CollectionReference thongBaoRef = db.collection("thongbao");

        thongBaoRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            // Lặp qua kết quả Firestore và thêm vào danh sách
                            for (DocumentSnapshot document : querySnapshot) {
                                // Chuyển tài liệu Firestore thành đối tượng ThongBao
                                ThongBao thongBao = document.toObject(ThongBao.class);
                                thongBao.setMaThongBao(document.getId()); // Lấy ID từ Firestore và gán vào đối tượng
                                thongBaoList.add(thongBao);
                            }

                            // Cập nhật adapter để hiển thị dữ liệu
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(NotificeScreen.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Thêm 3 dữ liệu tĩnh vào danh sách thông báo
    private void addStaticData() {
        thongBaoList.add(new ThongBao("1", "NV001", "Thông báo", "Họp về Push notification React Native", "2022-10-16", "Chưa đọc"));
        thongBaoList.add(new ThongBao("2", "NV002", "Thông báo", "Thông báo khác", "2022-10-17", "Đã đọc"));
        thongBaoList.add(new ThongBao("3", "NV003", "Thông báo", "Thông báo mới cập nhật", "2024-10-15", "Chưa đọc"));

        // Cập nhật lại adapter sau khi thêm dữ liệu
        adapter.notifyDataSetChanged();
    }
}
