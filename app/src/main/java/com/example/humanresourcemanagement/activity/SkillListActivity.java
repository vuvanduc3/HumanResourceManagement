package com.example.humanresourcemanagement.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.SkillAdapter;
import com.example.humanresourcemanagement.databinding.ActivitySkillBinding;
import com.example.humanresourcemanagement.firebase.skillconnet;
import com.example.humanresourcemanagement.model.Skills;

import java.util.ArrayList;
import java.util.List;

public class SkillListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSkill;
    private ActivitySkillBinding binding;
    private SkillAdapter skillAdapter;
    private List<Skills> skillsList = new ArrayList<>();
    private skillconnet skillconnet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySkillBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewSkill = findViewById(R.id.recyclerViewSkill);
        recyclerViewSkill.setLayoutManager(new LinearLayoutManager(this));

        skillAdapter = new SkillAdapter(skillsList);
        recyclerViewSkill.setAdapter(skillAdapter);

        skillAdapter.setOnItemClickListener(mask -> {
            Intent intent = new Intent(SkillListActivity.this, SkillDetailActivity.class);
            intent.putExtra("mask", mask);
            startActivityForResult(intent, 1);  // Sử dụng startActivityForResult
        });

        skillconnet = new skillconnet(this);
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SkillListActivity.this, HomeScreen.class);
            startActivity(intent);
        });

        binding.btnThem.setOnClickListener(v -> {
            Log.d("skill", "called");
            showAddSkillDialog();
        });

        loadSkillData();
    }

    private void loadSkillData() {
        skillconnet.getSkillList(new skillconnet.OnSkillListReceivedListener() {
            @Override
            public void onSkillListReceived(List<Skills> newSkillsList) {
                skillsList.clear();
                skillsList.addAll(newSkillsList);  // Thêm các kỹ năng vào danh sách hiển thị
                skillAdapter.notifyDataSetChanged();  // Cập nhật lại adapter để hiển thị dữ liệu mới
            }

            @Override
            public void onSkillListError(Exception e) {
                Log.e("SkillListActivity", "Error loading data", e);
            }
        });
    }


    private void showAddSkillDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_skill_add);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMask = dialog.findViewById(R.id.edtMask);
        EditText edtTensk = dialog.findViewById(R.id.edtTensk);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        skillconnet.getSkillList(new skillconnet.OnSkillListReceivedListener() {
            @Override
            public void onSkillListReceived(List<Skills> skillsList) {
                int maxId = 0;
                for (Skills skills : skillsList) {
                    String id = skills.getMask();
                    if (id.startsWith("sk")) {
                        try {
                            int numericId = Integer.parseInt(id.substring(2));
                            if (numericId > maxId) {
                                maxId = numericId;
                            }
                        } catch (NumberFormatException e) {

                        }
                    }
                }
                String newSkillId = "sk" + String.format("%02d", maxId + 1);
                tvMask.setText(newSkillId);
            }

            @Override
            public void onSkillListError(Exception e) {

            }
        });

        btnAdd.setOnClickListener(v -> {
            String mask = tvMask.getText().toString();
            String tensk = edtTensk.getText().toString();
            if (!mask.isEmpty() && !tensk.isEmpty()) {
                Skills newSkill = new Skills(mask, tensk);
                addSkillToDatabase(newSkill, dialog);
            } else {
                Log.w("BangCapListActivity", "Please fill in all fields.");
            }
        });

        dialog.show();
    }

    private void addSkillToDatabase(Skills  skills, Dialog dialog) {
      skillconnet.addSkill(skills, new skillconnet.OnOperationCompleteListener() {
            @Override
            public void onOperationSuccess() {
//                Log.d("BangCapListActivity", "BangCap successfully added!");
                loadSkillData();
                dialog.dismiss();
            }

            @Override
            public void onOperationError(Exception e) {
//                Log.e("BangCapListActivity", "Failed to add BangCap", e);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("reload", false)) {
                loadSkillData();  // Tải lại dữ liệu sau khi sửa
            }
        }
    }
}
