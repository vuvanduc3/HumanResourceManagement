package com.example.humanresourcemanagement.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.humanresourcemanagement.Fragment.DegreeFragment;
import com.example.humanresourcemanagement.Fragment.InfoDetailFragment;
import com.example.humanresourcemanagement.Fragment.SkillFragment;
import com.example.humanresourcemanagement.model.Employee;

public class EmployeeDetailAdapter extends FragmentStateAdapter {

    private Employee employee;

    public EmployeeDetailAdapter(@NonNull FragmentActivity fragmentActivity, Employee employee) {
        super(fragmentActivity);
        this.employee = employee;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("HHH", employee.toString());

        switch (position) {
            case 0:
                return InfoDetailFragment.newInstance(employee);
            case 1:
                return DegreeFragment.newInstance(employee);
            case 2:
                return SkillFragment.newInstance(employee);
            default:
                return InfoDetailFragment.newInstance(employee);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Số lượng tabs
    }
}

