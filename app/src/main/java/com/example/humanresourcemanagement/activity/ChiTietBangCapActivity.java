package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChiTietBangCap;

public class ChiTietBangCapActivity extends AppCompatActivity {
    private TextView tvDegreeId;
    private TextView tvIssueDate;
    private TextView tvGrade;
    private ImageView ivDegreeImage;
    private firebaseconnet firebaseConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_bangcapnv_detail);

        firebaseConnector = new firebaseconnet(this);
        initViews();
        setupToolbar();

        ChiTietBangCap bangCapNhanVien = getIntent().getParcelableExtra("bangcap_nv_data");
        if (bangCapNhanVien != null) {
            displayEmployeeDegreeDetails(bangCapNhanVien);
        }
    }

    private void initViews() {
        tvDegreeId = findViewById(R.id.tvDegreeId);
        tvIssueDate = findViewById(R.id.tvIssueDate);
        tvGrade = findViewById(R.id.tvGrade);
        ivDegreeImage = findViewById(R.id.ivDegreeImage);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void displayEmployeeDegreeDetails(ChiTietBangCap chiTietBangCapNhanVien) {
        firebaseConnector.getDegreeName(chiTietBangCapNhanVien.getBangcap_id(), new firebaseconnet.OnDegreeNameReceivedListener() {
            @Override
            public void onDegreeNameReceived(String degreeName) {


            }

            @Override
            public void onDegreeNameError(Exception e) {

            }
        });
        tvDegreeId.setText("Bằng cấp: " + chiTietBangCapNhanVien.getBangcap_id());
        tvIssueDate.setText("Ngày cấp: " + chiTietBangCapNhanVien.getNgaycap());
        tvGrade.setText("Mô tả: " + chiTietBangCapNhanVien.getMota() );
        if (chiTietBangCapNhanVien.getImageUrl() != null && !chiTietBangCapNhanVien.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(chiTietBangCapNhanVien.getImageUrl())
                    .into(ivDegreeImage);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}