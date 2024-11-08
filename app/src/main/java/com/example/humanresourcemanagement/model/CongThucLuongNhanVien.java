package com.example.humanresourcemanagement.model;

public class CongThucLuongNhanVien {
    private int id;
    private String maNhanVien;
    private double hsLuong;
    private double hsLuongTangCa;
    private double hsPcChucVu;
    private double hsPhucap;

    // Constructor
    public CongThucLuongNhanVien(int id, String maNhanVien, double hsLuong, double hsLuongTangCa, double hsPcChucVu, double hsPhucap) {
        this.id = id;
        this.maNhanVien = maNhanVien;
        this.hsLuong = hsLuong;
        this.hsLuongTangCa = hsLuongTangCa;
        this.hsPcChucVu = hsPcChucVu;
        this.hsPhucap = hsPhucap;
    }

    public CongThucLuongNhanVien() {
    }

    // Getters và Setters cho các thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public double getHsLuong() {
        return hsLuong;
    }

    public void setHsLuong(double hsLuong) {
        this.hsLuong = hsLuong;
    }

    public double getHsLuongTangCa() {
        return hsLuongTangCa;
    }

    public void setHsLuongTangCa(double hsLuongTangCa) {
        this.hsLuongTangCa = hsLuongTangCa;
    }

    public double getHsPcChucVu() {
        return hsPcChucVu;
    }

    public void setHsPcChucVu(double hsPcChucVu) {
        this.hsPcChucVu = hsPcChucVu;
    }

    public double getHsPhucap() {
        return hsPhucap;
    }

    public void setHsPhucap(double hsPhucap) {
        this.hsPhucap = hsPhucap;
    }

    // Phương thức trả về chi tiết công thức lương
    public String getCongThucLuongNhanVienDetails() {
        return "ID: " + id + ", Mã nhân viên: " + maNhanVien + ", Hệ số lương: " + hsLuong +
                ", Hệ số lương tăng ca: " + hsLuongTangCa + ", Hệ số phụ cấp chức vụ: " + hsPcChucVu +
                ", Hệ số phụ cấp khác: " + hsPhucap;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Công thức lương cho nhân viên: " + maNhanVien;
    }
}
