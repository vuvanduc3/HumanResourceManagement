package com.example.humanresourcemanagement.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.activity.ChiTietBangCapActivity;
import com.example.humanresourcemanagement.model.ChiTietBangCap;

import java.io.Serializable;
import java.util.List;

public class BangCapNVAdapter extends RecyclerView.Adapter<BangCapNVAdapter.EmployeeViewHolder> {

    private List<ChiTietBangCap> employeeDegreeList;
    private OnItemClickListener onItemClickListener;

    public BangCapNVAdapter(List<ChiTietBangCap> employeeDegreeList) {
        this.employeeDegreeList = employeeDegreeList;
    }

    public interface OnItemClickListener {
        void onItemClick(ChiTietBangCap bangCapNhanVien);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bangcapnv, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        ChiTietBangCap bangCapNhanVien = employeeDegreeList.get(position);
        holder.nameTextView.setText(bangCapNhanVien.getBangcap_id());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(bangCapNhanVien);
            }
            Intent intent = new Intent(v.getContext(), ChiTietBangCapActivity.class);
            intent.putExtra("bangcap_nv_data",  bangCapNhanVien);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return employeeDegreeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvTenBCNV);
        }
    }
}
