package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.PhongBan;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class phongBanfirebase {
    private static final String TAG = "EmployeeListActivity";
    private FirebaseFirestore db;

    // Constructor với Context
    public phongBanfirebase(Context context) {
        // Khởi tạo Firebase với Context
        FirebaseApp.initializeApp(context);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
    }

    // Phương thức để lấy danh sách phòng ban
    public void getPhongBanList(OnPhongBanListReceivedListener listener) {
        CollectionReference phongbanRef = db.collection("phongban");
        phongbanRef.get()
                .addOnCompleteListener(task -> {
                    List<PhongBan> phongBanList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PhongBan phongBan = document.toObject(PhongBan.class); // Chuyển đổi tài liệu thành đối tượng PhongBan
                            phongBanList.add(phongBan);

                        }
                        listener.onPhongBanListReceived(phongBanList);
                    } else {
                        Log.w("FirebaseConnect", "Error getting documents.", task.getException());
                        listener.onPhongBanListError(task.getException());
                    }
                });
    }

    public void getPhongBanById(String maPhongBan, OnPhongBanReceivedListener listener) {
        CollectionReference phongbanRef = db.collection("phongban");
        phongbanRef.whereEqualTo("maPhongBan", maPhongBan)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        PhongBan phongBan = document.toObject(PhongBan.class); // Chuyển tài liệu thành đối tượng Employee
                        listener.onPhongBanReceived(phongBan);
                    } else {
                        Log.w(TAG, "No phongban found with maPhongBan: " + maPhongBan);
                        listener.onPhongBanError(task.getException());
                    }
                });
    }



    public void deletePhongBanById(String maPhongBan, OnPhongBanDeleteListener listener) {
        CollectionReference phongbanRef = db.collection("phongban");
        phongbanRef.whereEqualTo("maPhongBan", maPhongBan)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Lấy tài liệu đầu tiên và xóa nó
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        phongbanRef.document(document.getId()).delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "PhongBan successfully deleted!");
                                    listener.onPhongBanDeleted();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error deleting phongban", e);
                                    listener.onPhongBanDeleteError(e);
                                });
                    } else {
                        Log.w(TAG, "No phongban found with maPhongBan: " + maPhongBan);
                        listener.onPhongBanDeleteError(task.getException());
                    }
                });
    }

    // Interface để nhận kết quả xóa PhongBan
    public interface OnPhongBanDeleteListener {
        void onPhongBanDeleted();
        void onPhongBanDeleteError(Exception e);
    }


    // Interface để nhận kết quả PhongBan
    public interface OnPhongBanReceivedListener {
        void onPhongBanReceived(PhongBan phongBan);

        void onPhongBanError(Exception e);
    }


    // Interface để nhận danh sách pb
    public interface OnPhongBanListReceivedListener {
        void onPhongBanListReceived(List<PhongBan> phongBanList);

        void onPhongBanListError(Exception e);
    }
}
