package com.example.humanresourcemanagement.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.Employee;


public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private OnItemClickListener onItemClickListener;

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    // Interface để lắng nghe sự kiện nhấp vào item
    public interface OnItemClickListener {
        void onItemClick(String employeeId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
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
        holder.nameTextView.setText(employee.getName());
        holder.positionTextView.setText(employee.getChucvuId());
        String imageUrl = employee.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.img.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_download_for_offline_24)
                    .error(R.drawable.baseline_error_outline_24)
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.baseline_error_outline_24);
        }

        // Xử lý sự kiện nhấp vào item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(employee.getEmployeeId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView positionTextView;
        ImageView img;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvEmployeeName);
            positionTextView = itemView.findViewById(R.id.tvEmployeeDepartment);
            img = itemView.findViewById(R.id.ivEmployeeImage);
        }
    }
}

