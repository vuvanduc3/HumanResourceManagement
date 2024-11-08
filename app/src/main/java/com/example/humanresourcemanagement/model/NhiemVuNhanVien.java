package com.example.humanresourcemanagement.model;

public class NhiemVuNhanVien {
    private String maNhiemVu;
    private String maNhanVien;

    // Constructor
    public NhiemVuNhanVien(String maNhiemVu, String maNhanVien) {
        this.maNhiemVu = maNhiemVu;
        this.maNhanVien = maNhanVien;
    }

    public NhiemVuNhanVien() {
    }

    // Getters và Setters cho các thuộc tính
    public String getMaNhiemVu() {
        return maNhiemVu;
    }

    public void setMaNhiemVu(String maNhiemVu) {
        this.maNhiemVu = maNhiemVu;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    // Phương thức trả về chi tiết nhiệm vụ nhân viên
    public String getNhiemVuNhanVienDetails() {
        return "Mã nhiệm vụ: " + maNhiemVu + ", Mã nhân viên: " + maNhanVien;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Nhiệm vụ ID: " + maNhiemVu + " - Nhân viên: " + maNhanVien;
    }
}
