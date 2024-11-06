package com.example.humanresourcemanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.ChucVu;
import java.util.List;

public class ChucVuAdapter extends RecyclerView.Adapter<ChucVuAdapter.ChucVuViewHolder> {

    private List<ChucVu> chucVuList;
    private OnItemClickListener onItemClickListener;

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(ChucVu chucVu);
    }

    // Hàm thiết lập OnItemClickListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ChucVuAdapter(List<ChucVu> chucVuList) {
        this.chucVuList = chucVuList;
    }

    @NonNull
    @Override
    public ChucVuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chucvu, parent, false);
        return new ChucVuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChucVuViewHolder holder, int position) {
        ChucVu chucVu = chucVuList.get(position);
        if (chucVu != null) {
            holder.tvChucVuId.setText(chucVu.getChucvu_id());
            holder.tvHsChucVu.setText(chucVu.getHschucvu());
            holder.tvLoaiChucVu.setText(chucVu.getLoaichucvu());

            // Gán sự kiện click cho item
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(chucVu);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chucVuList != null ? chucVuList.size() : 0;
    }

    public static class ChucVuViewHolder extends RecyclerView.ViewHolder {
        TextView tvChucVuId, tvHsChucVu, tvLoaiChucVu;

        public ChucVuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChucVuId = itemView.findViewById(R.id.tvMaCV);
            tvHsChucVu = itemView.findViewById(R.id.tvHeSoCV);
            tvLoaiChucVu = itemView.findViewById(R.id.tvTenChucVu);
        }
    }
}
