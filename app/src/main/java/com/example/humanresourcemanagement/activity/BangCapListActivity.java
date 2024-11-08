package com.example.humanresourcemanagement.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.BangCapAdapter;
import com.example.humanresourcemanagement.databinding.ActivityBangcapBinding;
import com.example.humanresourcemanagement.firebase.bangcapconnet;
import com.example.humanresourcemanagement.model.BangCap;

import java.util.ArrayList;
import java.util.List;

public class BangCapListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBangCap;
    private ActivityBangcapBinding binding;
    private BangCapAdapter bangCapAdapter;
    private List<BangCap> employeeList = new ArrayList<>();
    private bangcapconnet bangcapconnet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBangcapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewBangCap = findViewById(R.id.recyclerViewBangCap);
        recyclerViewBangCap.setLayoutManager(new LinearLayoutManager(this));

        bangCapAdapter = new BangCapAdapter(employeeList);
        recyclerViewBangCap.setAdapter(bangCapAdapter);

        bangCapAdapter.setOnItemClickListener(bangcap_id -> {
            Intent intent = new Intent(BangCapListActivity.this, BangCapDetailActivity.class);
            intent.putExtra("bangcap_id", bangcap_id);
            startActivityForResult(intent, 1);  // Sử dụng startActivityForResult
        });

        bangcapconnet = new bangcapconnet(this);
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(BangCapListActivity.this, HomeScreen.class);
            startActivity(intent);
        });

        binding.btnThem.setOnClickListener(v -> {
            Log.d("Bangcap", "called");
            showAddBangCapDialog();
        });

        loadBangCapData();
    }

    private void loadBangCapData() {
        bangcapconnet.getBangCapList(new bangcapconnet.OnBangCapListReceivedListener() {
            @Override
            public void onBangCapListReceived(List<BangCap> bangCapList) {
                employeeList.clear();
                employeeList.addAll(bangCapList);
                bangCapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBangCapListError(Exception e) {
                Log.e("BangCapList", "Error loading data", e);
            }
        });
    }

    private void showAddBangCapDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_bangcap_add);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMaBangCap = dialog.findViewById(R.id.edtBangCapId);
        EditText edtTenBangCap = dialog.findViewById(R.id.edtTenBang);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        bangcapconnet.getBangCapList(new bangcapconnet.OnBangCapListReceivedListener() {
            @Override
            public void onBangCapListReceived(List<BangCap> bangCapList) {
                int maxId = 0;
                for (BangCap bangCap : bangCapList) {
                    String id = bangCap.getBangcap_id();
                    if (id.startsWith("bc")) {
                        try {
                            int numericId = Integer.parseInt(id.substring(2));
                            if (numericId > maxId) {
                                maxId = numericId;
                            }
                        } catch (NumberFormatException e) {
                            Log.w("BangCapListActivity", "Invalid bangcap_id format: " + id);
                        }
                    }
                }
                String newBangCapId = "bc" + String.format("%02d", maxId + 1);
                tvMaBangCap.setText(newBangCapId);
            }

            @Override
            public void onBangCapListError(Exception e) {
                Log.w("BangCapListActivity", "Error fetching BangCap list", e);
            }
        });

        btnAdd.setOnClickListener(v -> {
            String maBangCap = tvMaBangCap.getText().toString();
            String tenBangCap = edtTenBangCap.getText().toString();
            if (!maBangCap.isEmpty() && !tenBangCap.isEmpty()) {
                BangCap newBangCap = new BangCap(maBangCap, tenBangCap);
                addBangCapToDatabase(newBangCap, dialog);
            } else {
                Log.w("BangCapListActivity", "Please fill in all fields.");
            }
        });

        dialog.show();
    }

    private void addBangCapToDatabase(BangCap bangCap, Dialog dialog) {
        bangcapconnet.addBangCap(bangCap, new bangcapconnet.OnOperationCompleteListener() {
            @Override
            public void onOperationSuccess() {
                Log.d("BangCapListActivity", "BangCap successfully added!");
                loadBangCapData();
                dialog.dismiss();
            }

            @Override
            public void onOperationError(Exception e) {
                Log.e("BangCapListActivity", "Failed to add BangCap", e);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("reload", false)) {
                loadBangCapData();  // Tải lại dữ liệu sau khi sửa
            }
        }
    }
}
