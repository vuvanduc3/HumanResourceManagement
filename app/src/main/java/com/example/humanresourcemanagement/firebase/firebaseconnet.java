package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class firebaseconnet {
    private static final String TAG = "EmployeeListActivity";
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
    // Phương thức để lấy danh sách nhân viên
    public void getEmployeeList(OnEmployeeListReceivedListener listener) {
        CollectionReference employeesRef = db.collection("employees");
        employeesRef.get()
                .addOnCompleteListener(task -> {
                    List<Employee> employeeList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Employee employee = document.toObject(Employee.class); // Chuyển đổi tài liệu thành đối tượng Employee
                            employeeList.add(employee);
                            Log.d(TAG, "getEmployeeList: " + employee.getImageUrl());
                        }
                        listener.onEmployeeListReceived(employeeList);
                    } else {
                        Log.w("FirebaseConnect", "Error getting documents.", task.getException());
                        listener.onEmployeeListError(task.getException());
                    }
                });
    }

    // Interface để nhận danh sách nhân viên
    public interface OnEmployeeListReceivedListener {
        void onEmployeeListReceived(List<Employee> employeeList);
        void onEmployeeListError(Exception e);
    }
}