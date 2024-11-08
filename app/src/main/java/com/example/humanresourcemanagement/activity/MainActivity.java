package com.example.humanresourcemanagement.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.Fragment.HomeFragment;
import com.example.humanresourcemanagement.Fragment.MenuProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_layout); // Layout của bạn

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Chuyển đến MenuProfileFragment lúc đầu
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // Fragment mặc định
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Chuyển đổi bằng if-else
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment(); // Khởi tạo HomeFragment
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new MenuProfileFragment(); // Khởi tạo MenuProfileFragment
            }
            // Bạn có thể thêm các else if cho nav_search, nav_notify nếu cần

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true; // Để đánh dấu rằng sự kiện đã được xử lý
        });
    }

    // Phương thức để tải Fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment); // R.id.frameLayout là nơi chứa Fragment
        transaction.addToBackStack(null); // Thêm vào back stack nếu muốn quay lại
        transaction.commit();
    }
}