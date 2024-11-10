package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;


import com.example.humanresourcemanagement.model.CCCD;
import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class CCCDFireBase {

    private static final String TAG = "CCCDFireBase";
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public CCCDFireBase(Context context) {
        // Khởi tạo Firebase với Context
        FirebaseApp.initializeApp(context);
        // Khởi tạo Firestore và FirebaseStorage
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    // Phương thức lấy dữ liệu CCCD từ Firestore
    public void getCCCDData(String cccdId, OnCCCDDataFetchedListener listener) {
        CollectionReference cccdRef = db.collection("cccd");
        cccdRef.whereEqualTo("cccdNumber", cccdId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        CCCD cccd = document.toObject(CCCD.class); // Chuyển tài liệu thành đối tượng CCCD
                        listener.onSuccess(cccd);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }



    public void addCCCD(String cccdNumber, String frontImage, String backImage, final OnCCCDAddListener listener) {
        CollectionReference cccdRef = db.collection("cccd");

        // Tạo một đối tượng Map chứa thông tin CCCD
        Map<String, Object> cccdData = new HashMap<>();
        cccdData.put("cccdNumber", cccdNumber);
        cccdData.put("frontImage", frontImage);
        cccdData.put("backImage", backImage);

        // Sử dụng cccdNumber làm document ID để tránh trùng lặp
        cccdRef.document(cccdNumber).set(cccdData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "CCCD đã được thêm thành công");
                    listener.onCCCDAdded();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Lỗi khi thêm CCCD", e);
                    listener.onCCCDAddError(e);
                });
    }

    public interface OnCCCDAddListener {
        void onCCCDAdded();
        void onCCCDAddError(Exception e);
    }

    // Interface callback để thông báo khi thao tác thành công hay thất bại
    public interface OnCCCDOperationCompleteListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    // Interface callback để lấy dữ liệu từ Firestore
    public interface OnCCCDDataFetchedListener {
        void onSuccess(CCCD cccd);
        void onFailure(Exception e);
    }

}
