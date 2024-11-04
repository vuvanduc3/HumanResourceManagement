package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class firebaseconnet {

    private FirebaseFirestore db;

    // Constructor với Context
    public firebaseconnet(Context context) {
        // Khởi tạo Firebase với Context
        FirebaseApp.initializeApp(context);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
    }

    // Phương thức để thêm dữ liệu vào Firestore
    public void addData() {
        // Tạo một Map để chứa dữ liệu
        Map<String, Object> user = new HashMap<>();
        user.put("first", "John");
        user.put("last", "Doe");
        user.put("born", 1990);

        // Thêm tài liệu vào collection "users"
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d("firebaseconnet", "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("firebaseconnet", "Error adding document", e);
                });
    }
}