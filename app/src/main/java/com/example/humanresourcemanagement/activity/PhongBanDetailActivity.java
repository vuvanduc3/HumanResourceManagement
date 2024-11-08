package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.databinding.ActivityPhongbanDetailBinding;
import com.example.humanresourcemanagement.firebase.phongBanfirebase;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.Employee;
import com.example.humanresourcemanagement.model.PhongBan;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import android.view.ViewGroup;

public class PhongBanDetailActivity extends AppCompatActivity {

    private ActivityPhongbanDetailBinding binding;
    private phongBanfirebase phongBanfirebase;
    private firebaseconnet firebaseConnectionEmployee;
    private List<Employee> employeeList = new ArrayList<>();
    private ArrayAdapter<Employee> employeeAdapter;

    String maQuanLy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhongbanDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy mã phòng ban từ Intent
        String maPhongBan = getIntent().getStringExtra("maPhongBan");

        // Khởi tạo Firebase
        phongBanfirebase = new phongBanfirebase(this);
        firebaseConnectionEmployee = new firebaseconnet(this);

        // Gọi hàm lấy thông tin phòng ban
        getPhongBanDetails(maPhongBan);

        // Xóa phòng ban
        binding.btnDelete.setOnClickListener(v -> deletePhongBan(maPhongBan));

        // Quay lại màn hình trước
        binding.btnBack.setOnClickListener(v -> finish());

        // Sự kiện nhấn nút "Edit"
        binding.btnEdit.setOnClickListener(v -> enableEditMode());

        // Sự kiện nhấn nút "Huy"
        binding.btnHuy.setOnClickListener(v -> huyEdit());


        // Sự kiện nhấn nút "Save"
        binding.btnSave.setOnClickListener(v -> savePhongBan(maPhongBan));
    }

    private void getPhongBanDetails(String maPhongBan) {
        phongBanfirebase.getPhongBanById(maPhongBan, new phongBanfirebase.OnPhongBanReceivedListener() {
            @Override
            public void onPhongBanReceived(PhongBan phongBan) {
                binding.tvMaPB.setText(phongBan.getMaPhongBan());
                binding.tvTenPB.setText(phongBan.getTenPhongBan());

                maQuanLy = phongBan.getMaQuanLy();
                if (maQuanLy != null && !maQuanLy.isEmpty()) {
                    firebaseConnectionEmployee.getEmployeeById(maQuanLy, new firebaseconnet.OnEmployeeReceivedListener() {
                        @Override
                        public void onEmployeeReceived(Employee employee) {
                            if (employee != null) {
                                binding.tvTenTP.setText(employee.getName());
                            } else {
                                binding.tvTenTP.setText("Không tìm thấy trưởng phòng");
                            }
                        }

                        @Override
                        public void onEmployeeError(Exception e) {
                            Log.e("PhongBanDetail", "Error fetching employee details", e);
                            binding.tvTenTP.setText("Không tìm thấy trưởng phòng");
                        }
                    });
                }

                // Lấy danh sách nhân viên cho spinner
                loadEmployeeList();
            }

            @Override
            public void onPhongBanError(Exception e) {
                Log.e("PhongBanDetail", "Error fetching phong ban details", e);
            }
        });
    }

    private void loadEmployeeList() {
        firebaseConnectionEmployee.getEmployeeList(new firebaseconnet.OnEmployeeListReceivedListener() {
            @Override
            public void onEmployeeListReceived(List<Employee> employees) {
                employeeList.clear();
                for (Employee employee : employees) {
                    if (!employee.getChucvuId().equals("GD") && !employee.getChucvuId().equals("TP")) {
                        employeeList.add(employee);
                    }
                }

                setupEmployeeSpinner(); // Thiết lập spinner với danh sách đã lọc

            }


            @Override
            public void onEmployeeListError(Exception e) {
                Log.e("PhongBanDetail", "Error fetching employee list", e);
            }
        });

    }

    private void setupEmployeeSpinner() {
        employeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employeeList);
        employeeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTruongPhong.setAdapter(employeeAdapter);
    }

    private void enableEditMode() {
        // Cho phép chỉnh sửa tên phòng ban
        binding.tvTenPB.setEnabled(true);
        binding.tvTenPB.setFocusableInTouchMode(true);
        binding.tvTenPB.requestFocus();
        binding.tvTenPB.setSelection(binding.tvTenPB.getText().length());
        // Hiện spinner và nút lưu

        binding.tvTenTP.setVisibility(View.GONE);
        binding.btnDelete.setVisibility(View.GONE);

        binding.spinnerTruongPhong.setVisibility(View.VISIBLE);
        binding.btnSave.setVisibility(View.VISIBLE);
        binding.btnHuy.setVisibility(View.VISIBLE);

    }


    private void huyEdit() {
        // Cho phép chỉnh sửa tên phòng ban
        binding.tvTenPB.setEnabled(false);


        binding.tvTenTP.setVisibility(View.VISIBLE);
        binding.btnDelete.setVisibility(View.VISIBLE);

        binding.spinnerTruongPhong.setVisibility(View.GONE);
        binding.btnSave.setVisibility(View.GONE);
        binding.btnHuy.setVisibility(View.GONE);

    }

    private void deletePhongBan(String maPhongBan) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phòng ban này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Nếu người dùng nhấn "Có", thực hiện xóa phòng ban
                    phongBanfirebase.deletePhongBanById(maPhongBan, new phongBanfirebase.OnPhongBanDeleteListener() {
                        @Override
                        public void onPhongBanDeleted() {
                            Toast.makeText(PhongBanDetailActivity.this, "Xóa phòng ban thành công!", Toast.LENGTH_SHORT).show();
                            finish(); // Trở lại màn hình trước đó hoặc đến một màn hình hiển thị danh sách phòng ban
                        }

                        @Override
                        public void onPhongBanDeleteError(Exception e) {
                            Log.e("PhongBanDetail", "Error deleting phong ban", e);
                            Toast.makeText(PhongBanDetailActivity.this, "Có lỗi xảy ra khi xóa phòng ban.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Cập nhật thông tin nhân viên
                    if (maQuanLy != null) {
                        phongBanfirebase.updateEmployeeDetails(maQuanLy, "NV", maPhongBan, new phongBanfirebase.OnEmployeeUpdateListener() {
                            @Override
                            public void onEmployeeUpdated() {
                                // Có thể thêm thông báo thành công ở đây nếu cần
                            }

                            @Override
                            public void onEmployeeUpdateError(Exception e) {
                                Log.e("PhongBanDetail", "Error updating employee chucVuId", e);
                                Toast.makeText(PhongBanDetailActivity.this, "Có lỗi xảy ra khi cập nhật chức vụ nhân viên.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss()) // Nếu người dùng nhấn "Không", chỉ đơn giản là đóng hộp thoại
                .show();
    }

    private void savePhongBan(String maPhongBan) {
        // Lấy thông tin từ view
        String tenPhongBan = binding.tvTenPB.getText().toString().trim();
        Employee selectedEmployee = (Employee) binding.spinnerTruongPhong.getSelectedItem();

        if (tenPhongBan.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin phòng ban
        PhongBan updatedPhongBan = new PhongBan();
        updatedPhongBan.setMaPhongBan(maPhongBan);
        updatedPhongBan.setTenPhongBan(tenPhongBan);
        if (selectedEmployee != null) {
            if (selectedEmployee.getEmployeeId() != maQuanLy) {
                updatedPhongBan.setMaQuanLy(selectedEmployee.getEmployeeId());
            }
        }


        // Cập nhật thông tin phòng ban trong Firestore
        phongBanfirebase.updatePhongBanById(maPhongBan, updatedPhongBan, new phongBanfirebase.OnPhongBanUpdateListener() {
            @Override
            public void onPhongBanUpdated() {
                Toast.makeText(PhongBanDetailActivity.this, "Cập nhật phòng ban thành công!", Toast.LENGTH_SHORT).show();
                huyEdit(); // Trở về chế độ không chỉnh sửa
                getPhongBanDetails(maPhongBan); // Lấy lại thông tin đã cập nhật
            }

            @Override
            public void onPhongBanUpdateError(Exception e) {
                Log.e("PhongBanDetail", "Error updating phong ban", e);
                Toast.makeText(PhongBanDetailActivity.this, "Có lỗi xảy ra khi cập nhật phòng ban.", Toast.LENGTH_SHORT).show();
            }
        });

        if (selectedEmployee!=null && selectedEmployee.getEmployeeId() != null) {

          if(maQuanLy!=null)
          {
              phongBanfirebase.updateEmployeeDetails(maQuanLy, "NV", maPhongBan, new phongBanfirebase.OnEmployeeUpdateListener() {
                  @Override
                  public void onEmployeeUpdated() {
                  }

                  @Override
                  public void onEmployeeUpdateError(Exception e) {
                      Log.e("PhongBanDetail", "Error updating employee chucVuId", e);
                      Toast.makeText(PhongBanDetailActivity.this, "Có lỗi xảy ra khi cập nhật chức vụ nhân viên.", Toast.LENGTH_SHORT).show();
                  }
              });

          }
            // Cập nhật chức vụ cho trưởng phòng
            phongBanfirebase.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "TP", maPhongBan, new phongBanfirebase.OnEmployeeUpdateListener() {
                @Override
                public void onEmployeeUpdated() {
                }

                @Override
                public void onEmployeeUpdateError(Exception e) {
                    Log.e("PhongBanDetail", "Error updating employee chucVuId", e);
                    Toast.makeText(PhongBanDetailActivity.this, "Có lỗi xảy ra khi cập nhật chức vụ nhân viên.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}