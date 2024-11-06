package com.example.humanresourcemanagement.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.humanresourcemanagement.databinding.AcivityBangcapDetailBinding;
import com.example.humanresourcemanagement.firebase.bangcapconnet;
import com.example.humanresourcemanagement.model.BangCap;

public class BangCapDetailActivity extends AppCompatActivity {

    private AcivityBangcapDetailBinding binding;
    private bangcapconnet bangcapconnet;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AcivityBangcapDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get bangcap_id from Intent
        documentId = getIntent().getStringExtra("bangcap_id");

        // Initialize Firebase
        bangcapconnet = new bangcapconnet(this);

        // Fetch BangCap details
        getBangCapDetails(documentId);

        // Handle Edit button click
        binding.btnEditCV.setOnClickListener(view -> toggleEditing());

        // Handle Save button click
        binding.btnSaveCV.setOnClickListener(view -> {
            if (areFieldsFilled()) {
                saveBangCapDetails();
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Delete button click
        binding.btnDeleteCV.setOnClickListener(view -> confirmDelete());

        // Handle Back button click
        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(BangCapDetailActivity.this, BangCapListActivity.class);
            startActivity(intent);
        });
    }

    private void getBangCapDetails(String bangcap_id) {
        bangcapconnet.getBangCapById(bangcap_id, new bangcapconnet.OnBangCapReceivedListener() {
            @Override
            public void onBangCapReceived(BangCap bangCap) {
                // Update UI with BangCap details
                binding.tvBangCapId.setText(bangCap.getBangcap_id());
                binding.tvTenBang.setText(bangCap.getTenBang());
            }

            @Override
            public void onBangCapError(Exception e) {
                Log.e("BangCapDetailActivity", "Error fetching BangCap details", e);
                Toast.makeText(BangCapDetailActivity.this, "Error fetching details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleEditing() {
        boolean isEditing = binding.tvTenBang.isEnabled();
        binding.tvTenBang.setEnabled(!isEditing);
        binding.btnDeleteCV.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        binding.btnSaveCV.setVisibility(isEditing ? View.GONE : View.VISIBLE);
    }

    private boolean areFieldsFilled() {
        return !binding.tvTenBang.getText().toString().isEmpty();
    }

    private void saveBangCapDetails() {
        String tenBang = binding.tvTenBang.getText().toString();
        BangCap updatedBangCap = new BangCap(documentId, tenBang); // Ensure BangCap has the required constructor

        bangcapconnet.updateBangCap(documentId, updatedBangCap, new bangcapconnet.OnOperationCompleteListener() {
            @Override
            public void onOperationSuccess() {
                Toast.makeText(BangCapDetailActivity.this, "BangCap updated successfully", Toast.LENGTH_SHORT).show();
                // Return to the previous activity and pass a result to reload data
                Intent resultIntent = new Intent();
                resultIntent.putExtra("reload", true);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close activity and return to list page
            }

            @Override
            public void onOperationError(Exception e) {
                Log.e("BangCapDetailActivity", "Error updating BangCap", e);
                Toast.makeText(BangCapDetailActivity.this, "Error updating BangCap", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this BangCap?")
                .setPositiveButton("Yes", (dialog, which) -> deleteBangCap())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteBangCap() {
        bangcapconnet.deleteBangCap(documentId, new bangcapconnet.OnOperationCompleteListener() {
            @Override
            public void onOperationSuccess() {
                Toast.makeText(BangCapDetailActivity.this, "BangCap deleted successfully", Toast.LENGTH_SHORT).show();
                // Return to the previous activity and pass a result to reload data
                Intent resultIntent = new Intent();
                resultIntent.putExtra("reload", true);
                setResult(RESULT_OK, resultIntent);
                finish(); // Close activity after deletion
            }

            @Override
            public void onOperationError(Exception e) {
                Log.e("BangCapDetailActivity", "Error deleting BangCap", e);
                Toast.makeText(BangCapDetailActivity.this, "Error deleting BangCap", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
