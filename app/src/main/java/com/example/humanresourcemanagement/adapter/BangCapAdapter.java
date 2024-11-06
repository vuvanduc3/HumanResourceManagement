package com.example.humanresourcemanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.BangCap;

import java.util.List;


public class BangCapAdapter extends RecyclerView.Adapter<BangCapAdapter.EmployeeViewHolder> {

    private List<BangCap> employeeList;
    private OnItemClickListener onItemClickListener;

    public BangCapAdapter(List<BangCap> employeeList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bangcap, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        BangCap employee = employeeList.get(position);
        holder.nameTextView.setText(employee.getBangcap_id());
        holder.positionTextView.setText(employee.getTenBang());
//
//
        // Xử lý sự kiện nhấp vào item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(employee.getBangcap_id());
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
            nameTextView = itemView.findViewById(R.id.tvBangCapId);
            positionTextView = itemView.findViewById(R.id.tvTenBang);

        }
    }
}

