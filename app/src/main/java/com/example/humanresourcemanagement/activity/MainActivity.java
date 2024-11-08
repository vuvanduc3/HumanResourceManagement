package com.example.humanresourcemanagement.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.Fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_layout); // Activity layout của bạn

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            transaction.replace(R.id.frameLayout, homeFragment); // fragment_container là ID của ViewGroup bạn đã thêm để chứa fragment
            transaction.commit();
        }
    }
}