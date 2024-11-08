package com.example.humanresourcemanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.ChiTietSkill;

import java.util.List;


public class SkillNVAdapter extends RecyclerView.Adapter<SkillNVAdapter.EmployeeViewHolder> {

    private List<ChiTietSkill> chiTietSkillList;
    private OnItemClickListener onItemClickListener;

    public SkillNVAdapter(List<ChiTietSkill> chiTietSkillList) {
        this.chiTietSkillList = chiTietSkillList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skillnv, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        ChiTietSkill chiTietSkill = chiTietSkillList.get(position);
        holder.nameTextView.setText(chiTietSkill.getMask());





    }

    @Override
    public int getItemCount() {
        return chiTietSkillList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;


        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvSkillnv);

        }
    }
}

