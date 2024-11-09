package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Toast;

import com.example.humanresourcemanagement.Fragment.HomeTPhongFragment;
import com.example.humanresourcemanagement.Fragment.MenuProfileFragment;
import com.example.humanresourcemanagement.Fragment.NotifiListFragment;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.Fragment.HomeFragment;
import com.example.humanresourcemanagement.model.Employee;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.humanresourcemanagement.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        employee = getIntent().getParcelableExtra("employee_data");

        // Mặc định chọn HomeFragment
        if (savedInstanceState == null) {
            if (employee.getChucvuId().equals("TP")) {
                loadFragment(HomeTPhongFragment.newInstance(employee));
            } else if (employee.getChucvuId().equals("GD")) {
                loadFragment(HomeFragment.newInstance(employee));
            }
            else {
                loadFragment(HomeFragment.newInstance(employee));
            }
        }

        // Xử lý chọn trên BottomNavigationView
        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                if (employee.getChucvuId().equals("TP")) {
                    selectedFragment = HomeTPhongFragment.newInstance(employee);
                } else {
                    selectedFragment = HomeFragment.newInstance(employee);
                }
            } else if (item.getItemId() == R.id.nav_notify) {
                selectedFragment = NotifiListFragment.newInstance(employee);
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = MenuProfileFragment.newInstance(employee);
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}