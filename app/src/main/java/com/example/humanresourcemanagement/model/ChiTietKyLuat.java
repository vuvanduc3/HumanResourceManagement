package com.example.humanresourcemanagement.model;

import java.util.Date;

public class ChiTietKyLuat {
    private int id;
    private String tieuDe;
    private Date ngayViPham;
    private String maNhanVien;
    private String noiDung;

    // Constructor
    public ChiTietKyLuat(int id, String tieuDe, Date ngayViPham, String maNhanVien, String noiDung) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.ngayViPham = ngayViPham;
        this.maNhanVien = maNhanVien;
        this.noiDung = noiDung;
    }

    public ChiTietKyLuat() {
    }

    // Getters và Setters cho các thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public Date getNgayViPham() {
        return ngayViPham;
    }

    public void setNgayViPham(Date ngayViPham) {
        this.ngayViPham = ngayViPham;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    // Phương thức trả về chi tiết kỷ luật
    public String getChiTietKyLuatDetails() {
        return "ID: " + id + ", Tiêu đề: " + tieuDe + ", Ngày vi phạm: " + ngayViPham +
                ", Mã nhân viên: " + maNhanVien + ", Nội dung: " + noiDung;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Kỷ luật: " + tieuDe + " - Nhân viên: " + maNhanVien;
    }
}
