package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.humanresourcemanagement.Fragment.MenuProfileFragment;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.Fragment.HomeFragment;

import com.example.humanresourcemanagement.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mặc định chọn HomeFragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Xử lý chọn trên BottomNavigationView
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_notify) {
                selectedFragment = new MenuProfileFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new MenuProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

    }

    // Hàm để thay thế Fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}
