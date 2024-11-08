package com.example.humanresourcemanagement.model;

import java.util.Date;

public class LuongThang {
    private String thang;
    private String maNhanVien;
    private double luongTangCa;
    private double thuong;
    private double thucNhan;
    private Date ngayThanhToan;
    private double phucap;

    // Constructor
    public LuongThang(String thang, String maNhanVien, double luongTangCa, double thuong, double thucNhan, Date ngayThanhToan, double phucap) {
        this.thang = thang;
        this.maNhanVien = maNhanVien;
        this.luongTangCa = luongTangCa;
        this.thuong = thuong;
        this.thucNhan = thucNhan;
        this.ngayThanhToan = ngayThanhToan;
        this.phucap = phucap;
    }

    public LuongThang() {
    }

    // Getters và Setters cho các thuộc tính
    public String getThang() {
        return thang;
    }

    public void setThang(String thang) {
        this.thang = thang;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public double getLuongTangCa() {
        return luongTangCa;
    }

    public void setLuongTangCa(double luongTangCa) {
        this.luongTangCa = luongTangCa;
    }

    public double getThuong() {
        return thuong;
    }

    public void setThuong(double thuong) {
        this.thuong = thuong;
    }

    public double getThucNhan() {
        return thucNhan;
    }

    public void setThucNhan(double thucNhan) {
        this.thucNhan = thucNhan;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public double getPhucap() {
        return phucap;
    }

    public void setPhucap(double phucap) {
        this.phucap = phucap;
    }

    // Phương thức trả về chi tiết lương tháng
    public String getLuongThangDetails() {
        return "Tháng: " + thang + ", Mã nhân viên: " + maNhanVien + ", Lương tăng ca: " + luongTangCa +
                ", Thưởng: " + thuong + ", Thực nhận: " + thucNhan + ", Ngày thanh toán: " + ngayThanhToan +
                ", Phụ cấp: " + phucap;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Lương tháng: " + thang + " - Nhân viên: " + maNhanVien;
    }
}
