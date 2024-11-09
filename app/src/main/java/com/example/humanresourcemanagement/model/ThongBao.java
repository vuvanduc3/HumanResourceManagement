package com.example.humanresourcemanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ThongBao implements Parcelable {
    private String maThongBao;
    private String maNhanVien;
    private String loaiThongBao;
    private String thongDiep;
    private String ngayThongBao;
    private String trangThai;

    // Constructor
    public ThongBao(String maThongBao, String maNhanVien, String loaiThongBao, String thongDiep, String ngayThongBao, String trangThai) {
        this.maThongBao = maThongBao;
        this.maNhanVien = maNhanVien;
        this.loaiThongBao = loaiThongBao;
        this.thongDiep = thongDiep;
        this.ngayThongBao = ngayThongBao;
        this.trangThai = trangThai != null ? trangThai : "Chưa đọc"; // Mặc định là "Chưa đọc"
    }

    public ThongBao() {
    }

    // Implement Parcelable
    protected ThongBao(Parcel in) {
        maThongBao = in.readString();
        maNhanVien = in.readString();
        loaiThongBao = in.readString();
        thongDiep = in.readString();
        ngayThongBao = in.readString();
        trangThai = in.readString();
    }

    public static final Creator<ThongBao> CREATOR = new Creator<ThongBao>() {
        @Override
        public ThongBao createFromParcel(Parcel in) {
            return new ThongBao(in);
        }

        @Override
        public ThongBao[] newArray(int size) {
            return new ThongBao[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maThongBao);
        dest.writeString(maNhanVien);
        dest.writeString(loaiThongBao);
        dest.writeString(thongDiep);
        dest.writeString(ngayThongBao);
        dest.writeString(trangThai);
    }

    // Getters và Setters cho các thuộc tính
    public String getMaThongBao() {
        return maThongBao;
    }

    public void setMaThongBao(String maThongBao) {
        this.maThongBao = maThongBao;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getLoaiThongBao() {
        return loaiThongBao;
    }

    public void setLoaiThongBao(String loaiThongBao) {
        this.loaiThongBao = loaiThongBao;
    }

    public String getThongDiep() {
        return thongDiep;
    }

    public void setThongDiep(String thongDiep) {
        this.thongDiep = thongDiep;
    }

    public String getNgayThongBao() {
        return ngayThongBao;
    }

    public void setNgayThongBao(String ngayThongBao) {
        this.ngayThongBao = ngayThongBao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Thông báo ID: " + maThongBao + " - Nhân viên: " + maNhanVien;
    }
}