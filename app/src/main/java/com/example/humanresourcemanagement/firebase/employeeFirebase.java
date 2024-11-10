package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class employeeFirebase {

    private FirebaseFirestore db;

    // Constructor với Context
    public employeeFirebase(Context context) {
        // Khởi tạo Firebase với Context
        FirebaseApp.initializeApp(context);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
    }

    public void changePassword(String employeeId, String currentPassword, String newPassword, OnPasswordChangeListener listener) {
        db.collection("employees")
                .whereEqualTo("employeeId", employeeId)
                .whereEqualTo("matKhau", currentPassword) // Kiểm tra mật khẩu hiện tại
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Cập nhật mật khẩu mới
                        document.getReference().update("matKhau", newPassword)
                                .addOnSuccessListener(aVoid -> {
                                    listener.onPasswordChangeSuccess();
                                })
                                .addOnFailureListener(e -> {
                                    listener.onPasswordChangeError(e);
                                });
                    } else {
                        listener.onPasswordChangeError(new Exception("Mật khẩu hiện tại không chính xác"));
                    }
                });
    }

    // Interface để nhận kết quả đổi mật khẩu
    public interface OnPasswordChangeListener {
        void onPasswordChangeSuccess();
        void onPasswordChangeError(Exception e);
    }



}
