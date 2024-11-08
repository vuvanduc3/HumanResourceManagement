package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;

import com.example.humanresourcemanagement.model.ChucVu;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.ChiTietBangCap;
import com.example.humanresourcemanagement.model.ChiTietSkill;
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

    // Phương thức để thêm chức vụ mới
    public void addChucVu(String chucvuId, String tenChucVu, String heSoChucVu, final OnChucVuAddListener listener) {
        CollectionReference chucVuRef = db.collection("chucvu");

        // Tạo một đối tượng ChucVu mới
        Map<String, Object> chucVuData = new HashMap<>();
        chucVuData.put("chucvu_id", chucvuId);
        chucVuData.put("loaichucvu", tenChucVu);
        chucVuData.put("hschucvu", heSoChucVu);

        // Thêm đối tượng vào collection
        chucVuRef.add(chucVuData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Chức vụ đã được thêm với ID: " + documentReference.getId());
                    listener.onChucVuAdded();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Lỗi khi thêm chức vụ", e);
                    listener.onChucVuAddError(e);
                });
    }

    // Phương thức để cập nhật thông tin nhân viên
    public void updateEmployee(String employeeId, Employee updatedEmployee, OnEmployeeUpdatedListener listener) {
        db.collection("employees").document(employeeId).set(updatedEmployee)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onEmployeeUpdated();  // Cập nhật thành công
                    } else {
                        listener.onError(task.getException());  // Cập nhật thất bại
                    }
                });
    }

    public void login(String employeeId, String matKhau, OnLoginListener listener) {
        db.collection("employees")
                .whereEqualTo("employeeId", employeeId)
                .whereEqualTo("matKhau", matKhau)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        Employee employee = document.toObject(Employee.class);

                        // Kiểm tra phân quyền dựa trên chucvuId
                        String chucvuId = employee.getChucvuId();
                        if ("GD".equals(chucvuId)) {
                            listener.onLoginSuccess(employee, "GD");
                        } else if ("TP".equals(chucvuId)) {
                            listener.onLoginSuccess(employee, "TP");
                        } else {
                            listener.onLoginSuccess(employee, "OTHER");
                        }
                    } else {
                        listener.onLoginError(new Exception("Login failed: Invalid employeeId or password"));
                    }
                });
    }

    public void getDegreeName(String bangcapId, OnDegreeNameReceivedListener listener) {
        CollectionReference bangCapRef = db.collection("bangcap");
        bangCapRef.whereEqualTo("bangcap_id", bangcapId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String degreeName = document.getString("tenBang"); // Assuming "name" is the degree name field
                        listener.onDegreeNameReceived(degreeName);
                    } else {
                        Log.w(TAG, "No degree found with bangcap_id: " + bangcapId);
                        listener.onDegreeNameError(new Exception("No degree found"));
                    }
                });
    }

    // Listener interface to handle degree name retrieval
    public interface OnDegreeNameReceivedListener {
        void onDegreeNameReceived(String degreeName);
        void onDegreeNameError(Exception e);
    }

    public void getSkillName(String mask, OnSkillNameReceivedListener listener) {
        CollectionReference skillRef = db.collection("skills");
        skillRef.whereEqualTo("mask", mask)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String degreeName = document.getString("tensk"); // Assuming "name" is the degree name field
                        listener.onSkillNameReceived(degreeName);
                    } else {
                        Log.w(TAG, "No degree found with bangcap_id: " + mask);
                        listener.onSkillNameError(new Exception("No degree found"));
                    }
                });
    }

    // Listener interface to handle degree name retrieval
    public interface OnSkillNameReceivedListener {
        void onSkillNameReceived(String skillName);
        void onSkillNameError(Exception e);
    }

    public void getSkillNhanVienById(String maNhanVien, OnSkillNVListReceivedListener listener) {
        CollectionReference skillnhanvienRef = db.collection("skillnhanvien");

        // Query all documents where employeeId matches the given employee ID
        skillnhanvienRef.whereEqualTo("employeeId", maNhanVien)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Create a list to store all degrees (ChiTietBangCap)
                        List<ChiTietSkill> chiTietSkillList = new ArrayList<>();

                        // Loop through all the documents returned by the query
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            ChiTietSkill chiTietSkill = document.toObject(ChiTietSkill.class); // Convert each document to ChiTietBangCap object
                            if (chiTietSkill != null) {
                                chiTietSkillList.add(chiTietSkill); // Add the degree to the list
                            }
                        }

                        // Pass the list to the listener
                        listener.onSkillNVListReceived(chiTietSkillList);
                    } else {
                        // Case when no documents are found
                        Log.w(TAG, "No Bang Cap found with maNhanVien: " + maNhanVien);

                        // If there's an exception, notify the listener
                        if (task.getException() != null) {
                            listener.onSkillNVListError(task.getException());
                        } else {
                            // If no data is found but no exception, pass a custom exception
                            listener.onSkillNVListError(new Exception("No data found for EmployeeId: " + maNhanVien));
                        }
                    }
                });
    }
    public void getBangCapNhanVienById(String maNhanVien, OnBangCapNVListReceivedListener listener) {
        CollectionReference bangcapnhanvienRef = db.collection("bangcapnhanvien");

        // Query all documents where employeeId matches the given employee ID
        bangcapnhanvienRef.whereEqualTo("employeeId", maNhanVien)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Create a list to store all degrees (ChiTietBangCap)
                        List<ChiTietBangCap> bangCapList = new ArrayList<>();

                        // Loop through all the documents returned by the query
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            ChiTietBangCap chiTietBangCap = document.toObject(ChiTietBangCap.class); // Convert each document to ChiTietBangCap object
                            if (chiTietBangCap != null) {
                                bangCapList.add(chiTietBangCap); // Add the degree to the list
                            }
                        }

                        // Pass the list to the listener
                        listener.onBangCapNVListReceived(bangCapList);
                    } else {
                        // Case when no documents are found
                        Log.w(TAG, "No Bang Cap found with maNhanVien: " + maNhanVien);

                        // If there's an exception, notify the listener
                        if (task.getException() != null) {
                            listener.onBangCapNVListError(task.getException());
                        } else {
                            // If no data is found but no exception, pass a custom exception
                            listener.onBangCapNVListError(new Exception("No data found for EmployeeId: " + maNhanVien));
                        }
                    }
                });
    }


    public interface OnSkillNVListReceivedListener {
        void onSkillNVListReceived(List<ChiTietSkill> chiTietSkillList);
        void onSkillNVListError(Exception e);
    }

    public interface OnSkillNVReceivedListener {
        void onSkillNVReceived(ChiTietSkill chiTietSkill);
        void onSkillNVError(Exception e);
    }

    public interface OnBangCapNVListReceivedListener {
        void onBangCapNVListReceived(List<ChiTietBangCap> chiTietBangCaps);
        void onBangCapNVListError(Exception e);
    }

    public interface OnBangCapNVReceivedListener {
        void onBangCapNVReceived(ChiTietBangCap chiTietBangCap);
        void onBangCapNVError(Exception e);
    }

    // Interface để nhận kết quả từ Firebase
    public interface OnEmployeeListReceivedListener {
        void onEmployeeListReceived(List<Employee> employeeList);
        void onEmployeeListError(Exception e);
    }

    public interface OnEmployeeReceivedListener {
        void onEmployeeReceived(Employee employee);
        void onEmployeeError(Exception e);
    }

    public interface OnEmployeeUpdatedListener {
        void onEmployeeUpdated();
        void onError(Exception e);
    }

    public interface OnChucVuListReceivedListener {
        void onChucVuListReceived(List<ChucVu> chucVuList);
        void onChucVuListError(Exception e);
    }

    public interface OnChucVuReceivedListener {
        void onChucVuReceived(ChucVu chucVu);
        void onChucVuError(Exception e);
    }

    public interface OnChucVuAddListener {
        void onChucVuAdded();
        void onChucVuAddError(Exception e);
    }

    public interface OnChucVuDeleteListener {
        void onChucVuDeleted();
        void onChucVuDeleteError(Exception e);
    }
    public interface OnLoginListener {
        void onLoginSuccess(Employee employee, String role);
        void onLoginError(Exception e);
    }


}
