package com.example.humanresourcemanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.humanresourcemanagement.R;
public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout); // Gán layout ở đây
    }
}