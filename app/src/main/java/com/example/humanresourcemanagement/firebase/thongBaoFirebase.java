package com.example.humanresourcemanagement.firebase;

import android.content.Context;
import android.util.Log;
import com.example.humanresourcemanagement.model.ThongBao;
import com.example.humanresourcemanagement.model.Employee;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class thongBaoFirebase {
    private DatabaseReference db;

    // Constructor với Context
    public thongBaoFirebase(Context context) {
        // Khởi tạo Firebase với Context
        FirebaseApp.initializeApp(context);

        // Khởi tạo Realtime Database
        db = FirebaseDatabase.getInstance().getReference("thongbaos");
    }

    public void addAllThongBao(ThongBao thongBao, List<Employee> danhSachNhanVien, OnThongBaoAddedListener listener) {
        Log.d("Thêm thong bao", "addAllThongBao: ");

        int[] successCount = {0};

        for (Employee nhanVien : danhSachNhanVien) {
            // Tạo khóa duy nhất cho thông báo
            String key = db.push().getKey();

            // Tạo một bản sao của thông báo cho từng nhân viên
            ThongBao thongBaoChoNhanVien = new ThongBao();
            thongBaoChoNhanVien.setMaThongBao(key);
            thongBaoChoNhanVien.setMaNhanVien(nhanVien.getEmployeeId());
            thongBaoChoNhanVien.setLoaiThongBao(thongBao.getLoaiThongBao());
            thongBaoChoNhanVien.setThongDiep(thongBao.getThongDiep());
            thongBaoChoNhanVien.setNgayThongBao(thongBao.getNgayThongBao());
            thongBaoChoNhanVien.setTrangThai("Chưa đọc");

            Log.d("----------thong bao", "addAllThongBao: "+thongBaoChoNhanVien);

            // Lưu thông báo vào Firebase Realtime Database
            db.child(key).setValue(thongBaoChoNhanVien)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("ThongBao", "Thông báo đã được gửi cho nhân viên: " + key);
                        successCount[0]++;
                        if (successCount[0] == danhSachNhanVien.size()) {
                            listener.onThongBaoAdded();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ThongBao", "Lỗi khi gửi thông báo cho nhân viên: " + e.getMessage());
                        listener.onThongBaoAddError(e);
                    });
        }
    }

    // lấy ds thông báo

    public void getThongBaos(OnThongBaosRetrievedListener listener) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ThongBao> thongBaoList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ThongBao thongBao = snapshot.getValue(ThongBao.class);
                    thongBaoList.add(thongBao);
                }

                listener.onThongBaosRetrieved(thongBaoList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onThongBaoRetrieveError(databaseError.toException());
            }
        });
    }

    public void updateThongBao(ThongBao thongBao, OnThongBaoUpdateListener listener) {
        // Sử dụng mã thông báo làm khóa để cập nhật
        db.child(thongBao.getMaThongBao()).child("trangThai").setValue("Đã đọc")
                .addOnSuccessListener(aVoid -> {
                    Log.d("ThongBao", "Cập nhật trạng thái thành công cho thông báo ID: " + thongBao.getMaThongBao());
                    listener.onUpdateSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("ThongBao", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
                    listener.onUpdateError(e);
                });
    }

    // Interface để nhận kết quả khi cập nhật thông báo
    public interface OnThongBaoUpdateListener {
        void onUpdateSuccess();
        void onUpdateError(Exception e);
    }
    // Interface for callback when notifications are retrieved
    public interface OnThongBaosRetrievedListener {
        void onThongBaosRetrieved(List<ThongBao> thongBaoList);
        void onThongBaoRetrieveError(Exception e);
    }



    // Interface để nhận kết quả khi thêm thông báo
    public interface OnThongBaoAddedListener {
        void onThongBaoAdded();
        void onThongBaoAddError(Exception e);
    }
}