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
    private static final String TAG = "Phong ban:";
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

    public void updatePhongBanById(String maPhongBan, PhongBan updatedPhongBan, OnPhongBanUpdateListener listener) {
        CollectionReference phongbanRef = db.collection("phongban");
        phongbanRef.whereEqualTo("maPhongBan", maPhongBan)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Lấy tài liệu hiện tại
                        PhongBan currentPhongBan = document.toObject(PhongBan.class);

                        // Tạo một Map để chứa các trường cần cập nhật
                        Map<String, Object> updates = new HashMap<>();

                        // So sánh và thêm các trường thay đổi vào Map
                        if (currentPhongBan.getTenPhongBan() != null && !currentPhongBan.getTenPhongBan().equals(updatedPhongBan.getTenPhongBan())) {
                            updates.put("tenPhongBan", updatedPhongBan.getTenPhongBan());
                        }
                        if (currentPhongBan.getMaQuanLy() != null && !currentPhongBan.getMaQuanLy().equals(updatedPhongBan.getMaQuanLy())) {
                            updates.put("maQuanLy", updatedPhongBan.getMaQuanLy());
                        }
                        // Thêm các trường khác nếu cần

                        if (!updates.isEmpty()) {
                            // Cập nhật các trường thay đổi
                            phongbanRef.document(document.getId()).update(updates)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "PhongBan successfully updated!");
                                        listener.onPhongBanUpdated();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error updating phongban", e);
                                        listener.onPhongBanUpdateError(e);
                                    });
                        } else {
                            Log.d(TAG, "No changes detected. PhongBan not updated.");
                            listener.onPhongBanUpdated(); // Hoặc tùy chọn khác nếu không có thay đổi
                        }
                    } else {
                        Log.w(TAG, "No phongban found with maPhongBan: " + maPhongBan);
                        listener.onPhongBanUpdateError(task.getException());
                    }
                });
    }

    public void updateEmployeeDetails(String employeeId, String newChucVuId, String newPhongBanId, OnEmployeeUpdateListener listener) {
        // Tạo một Map để chứa các trường cần cập nhật
        Map<String, Object> updates = new HashMap<>();
        updates.put("chucvuId", newChucVuId);
        updates.put("phongbanId", newPhongBanId);

        db.collection("employees")
                .document(employeeId) // Document ID của nhân viên
                .update(updates) // Cập nhật nhiều trường
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Employee details successfully updated!");
                    listener.onEmployeeUpdated();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error updating employee details", e);
                    listener.onEmployeeUpdateError(e);
                });
    }


    // Phương thức để thêm phòng ban
    public void addPhongBan(PhongBan phongBan, OnPhongBanAddedListener listener) {
        // Sử dụng maPhongBan làm ID tài liệu
        db.collection("phongban")
                .document(phongBan.getMaPhongBan()) // ID tài liệu là maPhongBan
                .set(phongBan) // Sử dụng phương thức set để thêm hoặc ghi đè dữ liệu
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "PhongBan added with ID: " + phongBan.getMaPhongBan());
                    listener.onPhongBanAdded();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding PhongBan", e);
                    listener.onPhongBanAddError(e);
                });
    }
    // Interface để nhận kết quả khi thêm phòng ban
    public interface OnPhongBanAddedListener {
        void onPhongBanAdded();
        void onPhongBanAddError(Exception e);
    }


    // Interface để nhận kết quả cập nhật nhân viên
    public interface OnEmployeeUpdateListener {
        void onEmployeeUpdated();
        void onEmployeeUpdateError(Exception e);
    }

    // Interface để nhận kết quả cập nhật PhongBan
    public interface OnPhongBanUpdateListener {
        void onPhongBanUpdated();
        void onPhongBanUpdateError(Exception e);
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
