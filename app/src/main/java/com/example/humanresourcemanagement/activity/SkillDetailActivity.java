package com.example.humanresourcemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanresourcemanagement.databinding.AcivitySkillDetailBinding;
import com.example.humanresourcemanagement.firebase.skillconnet;
import com.example.humanresourcemanagement.model.Skills;

public class SkillDetailActivity extends AppCompatActivity {

    private AcivitySkillDetailBinding binding;
    private skillconnet skillconnet;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AcivitySkillDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get bangcap_id from Intent
        documentId = getIntent().getStringExtra("mask");

        // Initialize Firebase
        skillconnet = new skillconnet(this);

        // Fetch BangCap details
        getSkillDetails(documentId);

        // Handle Edit button click
        binding.btnEditCV.setOnClickListener(view -> toggleEditing());

        // Handle Save button click
        binding.btnSaveCV.setOnClickListener(view -> {
            if (areFieldsFilled()) {
                saveSkillDetails();
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Delete button click
        binding.btnDeleteCV.setOnClickListener(view -> confirmDelete());

        // Handle Back button click
        binding.btnBack.setOnClickListener(view -> {
          finish();
        });
    }

    private void getSkillDetails(String mask) {
        skillconnet.getSkillById(mask, new skillconnet.OnSkillReceivedListener() {
            @Override
            public void onSkillReceived(Skills skills) {
                // Update UI with BangCap details
                binding.tvMask.setText(skills.getMask());
                binding.tvTensk.setText(skills.getTensk());
            }

            @Override
            public void onSkillError(Exception e) {
//                Log.e("BangCapDetailActivity", "Error fetching BangCap details", e);
                Toast.makeText(SkillDetailActivity.this, "Error fetching details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleEditing() {
        boolean isEditing = binding.tvTensk.isEnabled();
        binding.tvTensk.setEnabled(!isEditing);
        binding.btnDeleteCV.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        binding.btnSaveCV.setVisibility(isEditing ? View.GONE : View.VISIBLE);
    }

    private boolean areFieldsFilled() {
        return !binding.tvTensk.getText().toString().isEmpty();
    }

    private void saveSkillDetails() {
        String tensk = binding.tvTensk.getText().toString();
        Skills updatedSkill = new Skills(documentId, tensk); // Ensure BangCap has the required constructor

        skillconnet.updateSkill(documentId, updatedSkill, new skillconnet.OnOperationCompleteListener() {
            @Override
            public void onOperationSuccess() {
                Toast.makeText(SkillDetailActivity.this, "skill updated successfully", Toast.LENGTH_SHORT).show();
                // Return to the previous activity and pass a result to reload data
                Intent resultIntent = new Intent();
                resultIntent.putExtra("reload", true);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close activity and return to list page
            }

            @Override
            public void onOperationError(Exception e) {
                Log.e("BangCapDetailActivity", "Error updating skill", e);
                Toast.makeText(SkillDetailActivity.this, "Error updating skill", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this skill?")
                .setPositiveButton("Yes", (dialog, which) -> deleteSkill())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteSkill() {
        skillconnet.deleteSkill(documentId, new skillconnet.OnOperationCompleteListener() {
            @Override
            public void onOperationSuccess() {
                Toast.makeText(SkillDetailActivity.this, "skill deleted successfully", Toast.LENGTH_SHORT).show();
                // Return to the previous activity and pass a result to reload data
                Intent resultIntent = new Intent();
                resultIntent.putExtra("reload", true);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close activity after deletion
            }

            @Override
            public void onOperationError(Exception e) {
//                Log.e("BangCapDetailActivity", "Error deleting BangCap", e);
                Toast.makeText(SkillDetailActivity.this, "Error deleting BangCap", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
