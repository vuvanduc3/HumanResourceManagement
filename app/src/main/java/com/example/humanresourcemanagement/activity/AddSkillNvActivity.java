package com.example.humanresourcemanagement.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.firebase.skillconnet;
import com.example.humanresourcemanagement.model.Skills;

import java.util.ArrayList;
import java.util.List;

public class AddSkillNvActivity extends AppCompatActivity {
    private static final String TAG = "AddSkillActivity";
    private ProgressDialog progressDialog;
    private Spinner spinnerSkill;
    private List<Skills> skillsList;
    private firebaseconnet fbConnect;
    private skillconnet skillConnet;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> skillNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skillnv_add);

        // Initialize components
        initializeViews();
        setupSpinner();
        setupListeners();
    }

    private void initializeViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý...");
        spinnerSkill = findViewById(R.id.spinnerSkill);
        fbConnect = new firebaseconnet(this);
        skillConnet = new skillconnet(this);

        // Initialize lists
        skillsList = new ArrayList<>();
        skillNames = new ArrayList<>();
    }

    private void setupSpinner() {
        // Initialize adapter for spinner
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, skillNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSkill.setAdapter(spinnerAdapter);

        // Load skills from Firebase
        loadSkillsList();
    }

    private void setupListeners() {
        findViewById(R.id.btnSave).setOnClickListener(v -> validateAndSaveSkill());
        // Add back button listener if you have one
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadSkillsList() {
        skillConnet.getSkillList(new skillconnet.OnSkillListReceivedListener() {
            @Override
            public void onSkillListReceived(List<Skills> skills) {
                skillsList.clear();
                skillsList.addAll(skills);

                // Update skill names in spinner
                skillNames.clear();
                for (Skills skill : skills) {
                    skillNames.add(skill.getTensk()); // Assuming getTensk() returns the skill name
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSkillListError(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddSkillNvActivity.this,
                        "Lỗi khi tải kĩ năng: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAndSaveSkill() {
        if (spinnerSkill.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Vui lòng chọn kĩ năng", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        saveSkillInfo();
    }

    private void saveSkillInfo() {
        Skills selectedSkill = skillsList.get(spinnerSkill.getSelectedItemPosition());
        String employeeId = getIntent().getStringExtra("employeeId");

        if (employeeId == null || employeeId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã nhân viên", Toast.LENGTH_SHORT).show();
            return;
        }

        fbConnect.addSkillNhanVien(employeeId, selectedSkill.getMask(), new firebaseconnet.OnSkillAddListener() {
            @Override
            public void onSkillAdded() {
                progressDialog.dismiss();
                Toast.makeText(AddSkillNvActivity.this, "Thêm kĩ năng thành công", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("shouldRefresh", true);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onSkillAddError(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddSkillNvActivity.this,
                        "Lỗi khi thêm kĩ năng: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}