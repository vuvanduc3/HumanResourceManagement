package com.example.humanresourcemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.humanresourcemanagement.R;
import com.example.humanresourcemanagement.model.ThongBao;

import java.util.List;

public class ThongBaoAdapter extends ArrayAdapter<ThongBao> {

    public ThongBaoAdapter(Context context, List<ThongBao> thongBaoList) {
        super(context, 0, thongBaoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notification, parent, false);
        }

        ThongBao thongBao = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.tvNotificationTitle);
        TextView messageTextView = convertView.findViewById(R.id.tvNotificationMessage);
        TextView dateTextView = convertView.findViewById(R.id.tvNotificationDate);

        titleTextView.setText(thongBao.getLoaiThongBao());
        messageTextView.setText(thongBao.getThongDiep());
        dateTextView.setText(thongBao.getNgayThongBao());

        return convertView;
    }
}
