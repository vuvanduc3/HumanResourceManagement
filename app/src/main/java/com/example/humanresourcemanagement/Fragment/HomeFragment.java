package com.example.humanresourcemanagement.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.humanresourcemanagement.databinding.HomeLayoutBinding;
import com.example.humanresourcemanagement.activity.EmployeeListActivity;
import com.example.humanresourcemanagement.activity.ChucVuActivity;
import com.example.humanresourcemanagement.activity.BangCapListActivity;
import com.example.humanresourcemanagement.activity.SkillListActivity;
import com.example.humanresourcemanagement.activity.PhongBanListActivity;

public class HomeFragment extends Fragment {
    private HomeLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.lnNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EmployeeListActivity.class);
                startActivity(intent);
            }
        });

        binding.lnChucVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChucVuActivity.class);
                startActivity(intent);
            }
        });

        binding.lnBangCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BangCapListActivity.class);
                startActivity(intent);
            }
        });

        binding.lnThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificeScreen.class);
                startActivity(intent);
            }
        });

        binding.lnSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SkillListActivity.class);
                startActivity(intent);
            }
        });

        binding.lnPhongBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PhongBanListActivity.class);
                startActivity(intent);
            }
        });
    }
}