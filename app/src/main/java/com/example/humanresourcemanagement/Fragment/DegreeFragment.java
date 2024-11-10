package com.example.humanresourcemanagement.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.adapter.BangCapNVAdapter;
import com.example.humanresourcemanagement.adapter.SkillNVAdapter;
import com.example.humanresourcemanagement.firebase.firebaseconnet;
import com.example.humanresourcemanagement.model.ChiTietBangCap;
import com.example.humanresourcemanagement.model.ChiTietSkill;
import com.example.humanresourcemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DegreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DegreeFragment extends Fragment {

    private firebaseconnet firebaseconnet; // Initialize the firebaseconnet instance
    private RecyclerView recyclerViewDegrees;
    private BangCapNVAdapter degreeAdapter;
    private List<ChiTietBangCap> chiTietBangCaps = new ArrayList<>();

    public DegreeFragment() {
        // Required empty public constructor
    }

    public static DegreeFragment newInstance(Employee employee) {
        DegreeFragment fragment = new DegreeFragment();
        Bundle args = new Bundle();
        args.putParcelable("employee_data", employee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize firebaseconnet here
        firebaseconnet = new firebaseconnet(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_degree, container, false);

        // Initialize RecyclerView and Adapter
        recyclerViewDegrees = view.findViewById(R.id.recyclerViewDegrees);
        recyclerViewDegrees.setLayoutManager(new LinearLayoutManager(getContext()));
        degreeAdapter = new BangCapNVAdapter(chiTietBangCaps);
        recyclerViewDegrees.setAdapter(degreeAdapter);

        // Retrieve employee data from arguments
        Employee employee = getArguments().getParcelable("employee_data");
        if (employee != null) {
            getBangCapNV(employee.getId()); // Load degrees
        }

        return view;
    }

    private void getBangCapNV(String employeeId) {
        if (firebaseconnet != null) {
            firebaseconnet.getBangCapNhanVienById(employeeId, new firebaseconnet.OnBangCapNVListReceivedListener() {
                @Override
                public void onBangCapNVListReceived(List<ChiTietBangCap> receivedBangCapList) {
                    if (receivedBangCapList != null && !receivedBangCapList.isEmpty()) {
                        chiTietBangCaps.clear();
                        chiTietBangCaps.addAll(receivedBangCapList);
                        degreeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onBangCapNVListError(Exception e) {
                    Log.e("DegreeFragment", "Error retrieving degrees", e);
                }
            });
        } else {
            Log.e("DegreeFragment", "firebaseconnet is null");
        }
    }
}
