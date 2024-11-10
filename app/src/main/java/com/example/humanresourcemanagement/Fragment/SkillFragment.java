package com.example.humanresourcemanagement.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.humanresourcemanagement.activity.AddSkillNvActivity;
import com.example.humanresourcemanagement.adapter.SkillNVAdapter;
import com.example.humanresourcemanagement.databinding.FragmentSkillBinding;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChiTietSkill;
import com.example.humanresourcemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class SkillFragment extends Fragment {
    private static final int ADD_SKILL_REQUEST_CODE = 100;
    private static final String TAG = "SkillFragment";
    private static final String ARG_EMPLOYEE = "employee_data";

    private FragmentSkillBinding binding;
    private firebaseconnet firebaseConnector;
    private SkillNVAdapter skillNVAdapter;
    private final List<ChiTietSkill> chiTietSkillList = new ArrayList<>();
    private Employee employee;

    public static SkillFragment newInstance(Employee employee) {
        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EMPLOYEE, employee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseConnector = new firebaseconnet(requireContext());
        if (getArguments() != null) {
            employee = getArguments().getParcelable(ARG_EMPLOYEE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSkillBinding.inflate(inflater, container, false);

        initializeRecyclerView();
        setupAddSkillButton();
        loadEmployeeSkills();

        return binding.getRoot();
    }

    private void initializeRecyclerView() {
        binding.recyclerViewSkillNV.setLayoutManager(new LinearLayoutManager(getContext()));
        skillNVAdapter = new SkillNVAdapter(chiTietSkillList);
        binding.recyclerViewSkillNV.setAdapter(skillNVAdapter);
    }

    private void setupAddSkillButton() {
        binding.btnAddSK.setOnClickListener(view -> {
            if (employee != null) {
                Intent intent = new Intent(getContext(), AddSkillNvActivity.class);
                intent.putExtra("employeeId", employee.getId());
                startActivityForResult(intent, ADD_SKILL_REQUEST_CODE);
            }
        });
    }

    private void loadEmployeeSkills() {
        if (employee != null) {
            getSkillNV(employee.getId());
        } else {
            Log.e(TAG, "Employee data is null");
        }
    }

    private void getSkillNV(String employeeId) {
        firebaseConnector.getSkillNhanVienById(employeeId, new firebaseconnet.OnSkillNVListReceivedListener() {
            @Override
            public void onSkillNVListReceived(List<ChiTietSkill> receivedSkillList) {
                handleReceivedSkillList(receivedSkillList);
            }

            @Override
            public void onSkillNVListError(Exception e) {
                Log.e(TAG, "Error retrieving skills", e);
            }
        });
    }

    private void handleReceivedSkillList(List<ChiTietSkill> receivedSkillList) {
        if (receivedSkillList != null && !receivedSkillList.isEmpty()) {
            chiTietSkillList.clear();
            for (ChiTietSkill skill : receivedSkillList) {
                fetchAndUpdateSkillName(skill);
            }
        } else {
            Log.d(TAG, "No skills found for employee");
        }
    }

    private void fetchAndUpdateSkillName(ChiTietSkill skill) {
        firebaseConnector.getSkillName(skill.getMask(), new firebaseconnet.OnSkillNameReceivedListener() {
            @Override
            public void onSkillNameReceived(String skillName) {
                skill.setMask(skillName);
                chiTietSkillList.add(skill);
                skillNVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSkillNameError(Exception e) {
                Log.e(TAG, "Error retrieving skill name", e);
                skill.setMask("Unknown skill");
                chiTietSkillList.add(skill);
                skillNVAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SKILL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getBooleanExtra("shouldRefresh", false) && employee != null) {
                chiTietSkillList.clear();
                skillNVAdapter.notifyDataSetChanged();
                getSkillNV(employee.getId());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}