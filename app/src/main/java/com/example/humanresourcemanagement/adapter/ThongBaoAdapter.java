package com.example.humanresourcemanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.activity.ChiTietThongBaoActivity;
import com.example.humanresourcemanagement.model.ThongBao;
import com.example.humanresourcemanagement.firebase.thongBaoFirebase;


import java.util.List;

public class ThongBaoAdapter extends ArrayAdapter<ThongBao> {
    private thongBaoFirebase thongBaoFirebase;

    public ThongBaoAdapter(Context context, List<ThongBao> thongBaoList) {
        super(context, 0, thongBaoList);
        thongBaoFirebase = new thongBaoFirebase(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification, parent, false);
        }

        ThongBao thongBao = getItem(position);
        LinearLayout llItem = convertView.findViewById(R.id.llItemNofi);
        TextView titleTextView = convertView.findViewById(R.id.tvNotificationTitle);
        TextView messageTextView = convertView.findViewById(R.id.tvNotificationMessage);
        TextView dateTextView = convertView.findViewById(R.id.tvNotificationDate);

        titleTextView.setText(thongBao.getLoaiThongBao());
        String thongDiep = thongBao.getThongDiep();
        if (thongDiep.length() > 40) {
            thongDiep = thongDiep.substring(0, 40) + "..."; // Cắt chuỗi và thêm "..."
        }
        messageTextView.setText(thongDiep);
        dateTextView.setText(thongBao.getNgayThongBao());

        // Kiểm tra trạng thái và thay đổi màu nền
        if ("Chưa đọc".equals(thongBao.getTrangThai())) {
            llItem.setBackgroundResource(R.drawable.boder_notification_gray);
        }
        else {
            llItem.setBackgroundResource(R.drawable.border_notification);

        }

        // Thiết lập sự kiện nhấn vào mục thông báo
        convertView.setOnClickListener(v -> {
            thongBao.setTrangThai("Đã đọc"); // Cập nhật trạng thái trong đối tượng

            // Cập nhật thông báo trong Firebase
            thongBaoFirebase.updateThongBao(thongBao, new thongBaoFirebase.OnThongBaoUpdateListener() {
                @Override
                public void onUpdateSuccess() {
                    // Khởi động ChiTietThongBaoActivity
                    Intent intent = new Intent(getContext(), ChiTietThongBaoActivity.class);
                    intent.putExtra("thongBao", thongBao);
                    getContext().startActivity(intent); // Chỉ cần gọi startActivity
                }

                @Override
                public void onUpdateError(Exception e) {
                }
            });
        });

        return convertView;
    }
}