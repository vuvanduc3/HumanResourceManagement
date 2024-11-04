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
        String imageUrl =  employee.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.img.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_download_for_offline_24) // ảnh tạm trong lúc tải
                    .error(R.drawable.baseline_error_outline_24)             // ảnh hiển thị nếu tải thất bại
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.baseline_error_outline_24); // ảnh mặc định nếu không có URL
        }
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView positionTextView;
        ImageView img ;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvEmployeeName);
            positionTextView = itemView.findViewById(R.id.tvEmployeeDepartment);
            img = itemView.findViewById(R.id.ivEmployeeImage);
        }
    }
}
