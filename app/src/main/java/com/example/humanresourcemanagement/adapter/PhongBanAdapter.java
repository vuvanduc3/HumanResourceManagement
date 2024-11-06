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
import com.example.humanresourcemanagement.model.PhongBan;


public class PhongBanAdapter extends RecyclerView.Adapter<PhongBanAdapter.PhongBanViewHolder> {

    private List<PhongBan> listPhongBan;
    private OnItemClickListener onItemClickListener;

    public PhongBanAdapter(List<PhongBan> phongBanList) {
        this.listPhongBan = phongBanList;
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
    public PhongBanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phongban_layout, parent, false);
        return new PhongBanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongBanViewHolder holder, int position) {
        PhongBan phongBan = listPhongBan.get(position);
        holder.idTextView.setText(phongBan.getMaPhongBan());
        holder.nameTextView.setText(phongBan.getTenPhongBan());
        holder.maTPTextView.setText(phongBan.getMaQuanLy());




        // Xử lý sự kiện nhấp vào item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(phongBan.getMaPhongBan());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhongBan.size();
    }

    public static class PhongBanViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView maTPTextView;

        public PhongBanViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.tvMaPB);
            nameTextView = itemView.findViewById(R.id.tvTenPB);
            maTPTextView = itemView.findViewById(R.id.tvTenTP);
        }
    }
}

