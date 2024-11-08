package com.example.humanresourcemanagement.model;

import java.util.Date;

public class NhiemVu {
    private String maNhiemVu;
    private String maPhongBan;
    private String maNhanVien;
    private Date batDau;
    private Date ketThuc;
    private String chiTiet;

    // Constructor
    public NhiemVu(String maNhiemVu, String maPhongBan, String maNhanVien, Date batDau, Date ketThuc, String chiTiet) {
        this.maNhiemVu = maNhiemVu;
        this.maPhongBan = maPhongBan;
        this.maNhanVien = maNhanVien;
        this.batDau = batDau;
        this.ketThuc = ketThuc;
        this.chiTiet = chiTiet;
    }

    public NhiemVu() {
    }

    // Getters và Setters cho các thuộc tính
    public String getMaNhiemVu() {
        return maNhiemVu;
    }

    public void setMaNhiemVu(String maNhiemVu) {
        this.maNhiemVu = maNhiemVu;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getBatDau() {
        return batDau;
    }

    public void setBatDau(Date batDau) {
        this.batDau = batDau;
    }

    public Date getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(Date ketThuc) {
        this.ketThuc = ketThuc;
    }

    public String getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(String chiTiet) {
        this.chiTiet = chiTiet;
    }

    // Phương thức trả về chi tiết nhiệm vụ
    public String getNhiemVuDetails() {
        return "Mã nhiệm vụ: " + maNhiemVu + ", Mã phòng ban: " + maPhongBan + ", Mã nhân viên: " + maNhanVien +
                ", Bắt đầu: " + batDau + ", Kết thúc: " + ketThuc + ", Chi tiết: " + chiTiet;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Nhiệm vụ: " + maNhiemVu + " - Nhân viên: " + maNhanVien;
    }
}
