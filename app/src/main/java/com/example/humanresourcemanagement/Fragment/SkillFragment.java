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
import com.example.humanresourcemanagement.adapter.SkillNVAdapter;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChiTietSkill;
import com.example.humanresourcemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillFragment extends Fragment {

    private firebaseconnet firebaseconnet;
    // RecyclerView
    private RecyclerView recyclerViewSkillnv;
    private SkillNVAdapter Skillnvdapter;

    // Data list
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
        firebaseconnet = new firebaseconnet(getContext());  // Initialize firebaseconnet
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skill, container, false);

        // Initialize RecyclerView and Adapter
        recyclerViewSkillnv = view.findViewById(R.id.recyclerViewSkillNV);
        recyclerViewSkillnv.setLayoutManager(new LinearLayoutManager(getContext()));
        Skillnvdapter = new SkillNVAdapter(chiTietSkillList);
        recyclerViewSkillnv.setAdapter(Skillnvdapter);

        // Retrieve employee data from arguments
        Employee employee = getArguments().getParcelable("employee_data");
        if (employee != null) {
            getSkillNV(employee.getId());  // Load skills for this employee
        }

        return view;
    }

    // Lấy thông tin kỹ năng nhân viên từ Firebase
    private void getSkillNV(String employeeId) {
        firebaseconnet.getSkillNhanVienById(employeeId, new firebaseconnet.OnSkillNVListReceivedListener() {
            @Override
            public void onSkillNVListReceived(List<ChiTietSkill> receivedSkillList) {
                if (receivedSkillList != null && !receivedSkillList.isEmpty()) {
                    chiTietSkillList.clear();  // Clear existing data
                    for (ChiTietSkill skill : receivedSkillList) {
                        String mask = skill.getMask();
                        firebaseconnet.getSkillName(mask, new firebaseconnet.OnSkillNameReceivedListener() {
                            @Override
                            public void onSkillNameReceived(String skillName) {
                                skill.setMask(skillName);  // Set the actual skill name
                                chiTietSkillList.add(skill);  // Add updated skill to the list
                                Skillnvdapter.notifyDataSetChanged();  // Notify the adapter of changes
                            }

                            @Override
                            public void onSkillNameError(Exception e) {
                                Log.e("EmployeeDetail", "Error retrieving skill name", e);
                                skill.setMask("No skill name");  // Default text on error
                                chiTietSkillList.add(skill);
                                Skillnvdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Log.d("EmployeeDetail", "No skills found for employee");
                }
            }

            @Override
            public void onSkillNVListError(Exception e) {
                Log.e("EmployeeDetail", "Error retrieving skills", e);
            }
        });
    }
}
