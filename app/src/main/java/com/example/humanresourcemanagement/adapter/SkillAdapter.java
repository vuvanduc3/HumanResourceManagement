package com.example.humanresourcemanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.Skills;

import java.util.List;


public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    private List<Skills> skillsList;
    private OnItemClickListener onItemClickListener;

    public SkillAdapter(List<Skills> skillsList) {
        this.skillsList = skillsList;
    }

    // Interface để lắng nghe sự kiện nhấp vào item
    public interface OnItemClickListener {
        void onItemClick(String mask);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        Skills skills = skillsList.get(position);
        holder.nameTextView.setText(skills.getMask());
        holder.positionTextView.setText(skills.getTensk());
//
//
        // Xử lý sự kiện nhấp vào item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(skills.getMask());
            }
        });
    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

    public static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView positionTextView;
        ImageView img;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvMask);
            positionTextView = itemView.findViewById(R.id.tvTensk);

        }
    }
}

