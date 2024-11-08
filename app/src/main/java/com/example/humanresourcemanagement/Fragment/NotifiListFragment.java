package com.example.humanresourcemanagement.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.ThongBaoAdapter;
import com.example.humanresourcemanagement.model.ThongBao;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotifiListFragment extends Fragment {

    private ListView listView;
    private ThongBaoAdapter adapter;
    private List<ThongBao> thongBaoList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout cho fragment
        View view = inflater.inflate(R.layout.activity_notifice_screen, container, false);

        // Khởi tạo view và Firebase Firestore
        listView = view.findViewById(R.id.listViewNotifications);
        db = FirebaseFirestore.getInstance();

        thongBaoList = new ArrayList<>();
        adapter = new ThongBaoAdapter(getContext(), thongBaoList); // Thay 'this' bằng 'getContext()'
        listView.setAdapter(adapter);

        // Thêm 3 thông báo tĩnh
        addStaticData();

        // Lấy danh sách thông báo từ Firebase
        loadThongBaoFromFirebase();

        return view;
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
                        Toast.makeText(getContext(), "Error getting documents.", Toast.LENGTH_SHORT).show();
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