package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.databinding.ActivityAddThongbaoBinding;
import com.example.humanresourcemanagement.firebase.phongBanfirebase;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.firebase.thongBaoFirebase;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.PhongBan;
import com.example.humanresourcemanagement.model.ThongBao;
import com.google.firebase.FirebaseApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

public class AddThongBaoActivity extends AppCompatActivity {

    private ActivityAddThongbaoBinding binding;
    private phongBanfirebase phongBanfirebase;
    private ArrayAdapter<PhongBan> phongBanAdapter;
    private List<PhongBan> phongBanList = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();
    private firebaseconnet firebaseConnectionEmployee;
    private thongBaoFirebase thongBaoFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddThongbaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         thongBaoFirebase = new thongBaoFirebase(this);

        phongBanfirebase = new phongBanfirebase(this);
        firebaseConnectionEmployee = new firebaseconnet(this);

        // Quay lại màn hình trước
        binding.btnBack.setOnClickListener(v -> finish());

        loadEmployeeList();
        // load ds phòng ban
        loatPBList();

        binding.btnAdd.setOnClickListener(v -> {
            addThongBao();
        });


    }
    private void addThongBao() {
        String loaiThongBao = binding.edtTieuDe.getText().toString().trim();
        String thongDiep = binding.edtNoiDung.getText().toString().trim();
        String maPhongBan = ((PhongBan) binding.spinnerPhongBan.getSelectedItem()).getMaPhongBan();

        if (thongDiep.isEmpty() || loaiThongBao.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ThongBao thongBao = new ThongBao(null, null, loaiThongBao, thongDiep, getCurrentDate(), null);

        if ("all".equals(maPhongBan)) {
            thongBaoFirebase.addAllThongBao(thongBao, employeeList, new thongBaoFirebase.OnThongBaoAddedListener() {
                @Override
                public void onThongBaoAdded() {
                    Toast.makeText(AddThongBaoActivity.this, "Thông báo đã được gửi tới tất cả nhân viên", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onThongBaoAddError(Exception e) {
                    Toast.makeText(AddThongBaoActivity.this, "Lỗi khi gửi thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            List<Employee> employeesInPhongBan = getEmployeesInPhongBan(maPhongBan);
            thongBaoFirebase.addAllThongBao(thongBao, employeesInPhongBan, new thongBaoFirebase.OnThongBaoAddedListener() {
                @Override
                public void onThongBaoAdded() {
                    String tenPhongBan = "";
                    for (PhongBan phongBan : phongBanList) {
                        if (phongBan.getMaPhongBan().equals(maPhongBan)) {
                            tenPhongBan = phongBan.getTenPhongBan(); // Giả sử có phương thức getTenPhongBan()
                            break;
                        }
                    }
                    Toast.makeText(AddThongBaoActivity.this, "Thông báo đã được gửi tới phòng ban "+tenPhongBan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onThongBaoAddError(Exception e) {
                    Toast.makeText(AddThongBaoActivity.this, "Lỗi khi gửi thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        Toast.makeText(this, "Đang gửi thông báo...", Toast.LENGTH_SHORT).show();
    }

    // Phương thức để lấy danh sách nhân viên trong phòng ban
    private List<Employee> getEmployeesInPhongBan(String maPhongBan) {
        List<Employee> employeesInPhongBan = new ArrayList<>();
        for (Employee emp : employeeList) {
            if (emp.getPhongbanId().equals(maPhongBan)) {
                employeesInPhongBan.add(emp);
            }
        }
        return employeesInPhongBan;
    }

    // Phương thức để lấy ngày hiện tại (định dạng tùy chỉnh)
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }




    private void loadEmployeeList() {
        firebaseConnectionEmployee.getEmployeeList(new firebaseconnet.OnEmployeeListReceivedListener() {
            @Override
            public void onEmployeeListReceived(List<Employee> employees) {
                employeeList.clear();
                employeeList.addAll(employees);


                Log.d("-------------ds-------", "onEmployeeListReceived: "+employeeList);

            }


            @Override
            public void onEmployeeListError(Exception e) {
                Log.e("PhongBanDetail", "Error fetching employee list", e);
            }
        });

    }




    // lấy ds phòng ban cho spinner
    private void loatPBList() {
        phongBanfirebase.getPhongBanList(new phongBanfirebase.OnPhongBanListReceivedListener() {
            @Override
            public void onPhongBanListReceived(List<PhongBan> phongBans) {
                phongBanList.clear();
                phongBanList.add(new PhongBan("all","","Tất cả"));

                Log.d("------dspb", "onPhongBanListReceived: "+phongBans);
                phongBanList.addAll(phongBans);
                setUpPhongBanAdapter();
            }

            @Override
            public void onPhongBanListError(Exception e) {

            }
        });

    }
    private void setUpPhongBanAdapter() {
        phongBanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, phongBanList);
        phongBanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPhongBan.setAdapter(phongBanAdapter);
        binding.spinnerPhongBan.setSelection(0);


    }







}