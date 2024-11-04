package com.example.humanresourcemanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.Employee;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList; // Sử dụng kiểu Employee thay vì Map

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.nameTextView.setText(employee.getName()); // Truy cập thông tin qua đối tượng Employee
        holder.positionTextView.setText(employee.getChucvuId()); // Giả sử có trường "chucvuId" tương ứng với vị trí
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView positionTextView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvEmployeeName);
            positionTextView = itemView.findViewById(R.id.tvEmployeeDepartment);
        }
    }
}
