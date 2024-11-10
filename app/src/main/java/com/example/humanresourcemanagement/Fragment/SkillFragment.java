package com.example.humanresourcemanagement.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.activity.AddSkillNvActivity;
import com.example.humanresourcemanagement.adapter.SkillNVAdapter;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChiTietSkill;
import com.example.humanresourcemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import com.example.humanresourcemanagement.databinding.FragmentSkillBinding;

public class SkillFragment extends Fragment {

    private FragmentSkillBinding binding;
    private firebaseconnet firebaseconnet;
    private SkillNVAdapter skillNVAdapter;
    private List<ChiTietSkill> chiTietSkillList = new ArrayList<>();

    public SkillFragment() {
        // Required empty public constructor
    }

    public static SkillFragment newInstance(Employee employee) {
        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee_data", employee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseconnet = new firebaseconnet(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSkillBinding.inflate(inflater, container, false);

        // Setup RecyclerView and Adapter
        binding.recyclerViewSkillNV.setLayoutManager(new LinearLayoutManager(getContext()));
        skillNVAdapter = new SkillNVAdapter(chiTietSkillList);
        binding.recyclerViewSkillNV.setAdapter(skillNVAdapter);

        // Load employee skills
        Employee employee = getArguments().getParcelable("employee_data");
        if (employee != null) {
            getSkillNV(employee.getId());
        }

        // Add click listener to the "Add Skill" button
        binding.btnAddSK.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddSkillNvActivity.class);
            intent.putExtra("employeeId", employee.getEmployeeId());
            startActivity(intent);
        });

        return binding.getRoot();
    }

    // Retrieve employee skills from Firebase
    private void getSkillNV(String employeeId) {
        firebaseconnet.getSkillNhanVienById(employeeId, new firebaseconnet.OnSkillNVListReceivedListener() {
            @Override
            public void onSkillNVListReceived(List<ChiTietSkill> receivedSkillList) {
                if (receivedSkillList != null && !receivedSkillList.isEmpty()) {
                    chiTietSkillList.clear();
                    for (ChiTietSkill skill : receivedSkillList) {
                        String mask = skill.getMask();
                        firebaseconnet.getSkillName(mask, new firebaseconnet.OnSkillNameReceivedListener() {
                            @Override
                            public void onSkillNameReceived(String skillName) {
                                skill.setMask(skillName);
                                chiTietSkillList.add(skill);
                                skillNVAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onSkillNameError(Exception e) {
                                Log.e("SkillFragment", "Error retrieving skill name", e);
                                skill.setMask("No skill name");
                                chiTietSkillList.add(skill);
                                skillNVAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Log.d("SkillFragment", "No skills found for employee");
                }
            }

            @Override
            public void onSkillNVListError(Exception e) {
                Log.e("SkillFragment", "Error retrieving skills", e);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}