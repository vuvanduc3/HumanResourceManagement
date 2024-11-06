package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.example.humanresourcemanagement.model.ChucVu;
import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public void getEmployeeById(String employeeId, OnEmployeeReceivedListener listener) {
        CollectionReference employeesRef = db.collection("employees");
        employeesRef.whereEqualTo("employeeId", employeeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        Employee employee = document.toObject(Employee.class); // Chuyển tài liệu thành đối tượng Employee
                        listener.onEmployeeReceived(employee);
                    } else {
                        Log.w(TAG, "No employee found with employeeId: " + employeeId);
                        listener.onEmployeeError(task.getException());
                    }
                });
    }

    // Phương thức để lấy danh sách chức vụ
    public void getChucVuList(OnChucVuListReceivedListener listener) {
        CollectionReference chucVuRef = db.collection("chucvu");
        chucVuRef.get()
                .addOnCompleteListener(task -> {
                    List<ChucVu> chucVuList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ChucVu chucVu = document.toObject(ChucVu.class);
                            chucVuList.add(chucVu);
                            Log.d(TAG, "getChucVuList: " + chucVu.getLoaichucvu());
                        }
                        listener.onChucVuListReceived(chucVuList);
                    } else {
                        Log.w(TAG, "Error getting chucvus.", task.getException());
                        listener.onChucVuListError(task.getException());
                    }
                });
    }

    // Phương thức để lấy chi tiết chức vụ theo chucvuId
    public void getChucVuById(String chucvuId, OnChucVuReceivedListener listener) {
        CollectionReference chucVuRef = db.collection("chucvu");
        chucVuRef.whereEqualTo("chucvu_id", chucvuId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        ChucVu chucVu = document.toObject(ChucVu.class);
                        listener.onChucVuReceived(chucVu);
                    } else {
                        Log.w(TAG, "No ChucVu found with chucvuId: " + chucvuId);
                        listener.onChucVuError(task.getException());
                    }
                });
    }
    // Phương thức để cập nhật thông tin chức vụ
    public void editChucVu(String chucvuId, String newHeSoChucVu,  final OnChucVuReceivedListener listener) {
        CollectionReference chucVuRef = db.collection("chucvu");

        // Tìm chức vụ dựa trên chucvuId
        chucVuRef.whereEqualTo("chucvu_id", chucvuId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        // Lấy reference của document
                        document.getReference().update(
                                "hschucvu", newHeSoChucVu

                        ).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Log.d(TAG, "Chuc Vu updated successfully");
                                // Trả về đối tượng ChucVu đã được cập nhật
                                ChucVu updatedChucVu = document.toObject(ChucVu.class);
                                listener.onChucVuReceived(updatedChucVu);
                            } else {
                                Log.w(TAG, "Error updating Chuc Vu", updateTask.getException());
                                listener.onChucVuError(updateTask.getException());
                            }
                        });
                    } else {
                        Log.w(TAG, "No Chuc Vu found with chucvuId: " + chucvuId);
                        listener.onChucVuError(task.getException());
                    }
                });
    }
    // Phương thức để xóa chức vụ dựa trên chucvu_id
    public void deleteChucVu(String chucvuId, final OnChucVuDeleteListener listener) {
        CollectionReference chucVuRef = db.collection("chucvu");

        // Tìm chức vụ dựa trên chucvu_id
        chucVuRef.whereEqualTo("chucvu_id", chucvuId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        // Xóa tài liệu tìm thấy
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Chuc Vu deleted successfully");
                                    listener.onChucVuDeleted();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, "Error deleting Chuc Vu", e);
                                    listener.onChucVuDeleteError(e);
                                });
                    } else {
                        Log.w(TAG, "No Chuc Vu found with chucvuId: " + chucvuId);
                        listener.onChucVuDeleteError(task.getException());
                    }
                });
    }

    // Interface để nhận kết quả xóa chức vụ
    public interface OnChucVuDeleteListener {
        void onChucVuDeleted();
        void onChucVuDeleteError(Exception e);
    }

    // Interface để nhận danh sách chức vụ
    public interface OnChucVuListReceivedListener {
        void onChucVuListReceived(List<ChucVu> chucVuList);

        void onChucVuListError(Exception e);

    }

    // Interface để nhận chi tiết chức vụ
    public interface OnChucVuReceivedListener {
        void onChucVuReceived(ChucVu chucVu);

        void onChucVuError(Exception e);
    }


    // Interface để nhận kết quả Employee
    public interface OnEmployeeReceivedListener {
        void onEmployeeReceived(Employee employee);

        void onEmployeeError(Exception e);
    }


    // Interface để nhận danh sách nhân viên
    public interface OnEmployeeListReceivedListener {
        void onEmployeeListReceived(List<Employee> employeeList);

        void onEmployeeListError(Exception e);
    }
}
