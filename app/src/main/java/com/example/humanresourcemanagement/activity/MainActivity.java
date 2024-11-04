package com.example.humanresourcemanagement.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.humanresourcemanagement.firebase.firebaseconnet;

import com.example.humanresourcemanagement.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.bottom_layout);

        // Khởi tạo kết nối Firebase và truyền context
        firebaseconnet firebaseConnection = new firebaseconnet(this);
        firebaseConnection.addData();  // Gọi phương thức để thêm dữ liệu

    }
}