package com.example.humanresourcemanagement.model;

import java.util.Date;

public class ChiTietKhenThuong {
    private int id;
    private String tieuDe;
    private Date ngayKhenThuong;
    private String noiDung;
    private String maNhanVien;
    private double tienThuong;

    // Constructor
    public ChiTietKhenThuong(int id, String tieuDe, Date ngayKhenThuong, String noiDung, String maNhanVien, double tienThuong) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.ngayKhenThuong = ngayKhenThuong;
        this.noiDung = noiDung;
        this.maNhanVien = maNhanVien;
        this.tienThuong = tienThuong;
    }

    public ChiTietKhenThuong() {
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

    public Date getNgayKhenThuong() {
        return ngayKhenThuong;
    }

    public void setNgayKhenThuong(Date ngayKhenThuong) {
        this.ngayKhenThuong = ngayKhenThuong;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public double getTienThuong() {
        return tienThuong;
    }

    public void setTienThuong(double tienThuong) {
        this.tienThuong = tienThuong;
    }

    // Phương thức trả về chi tiết khen thưởng
    public String getChiTietKhenThuongDetails() {
        return "ID: " + id + ", Tiêu đề: " + tieuDe + ", Ngày khen thưởng: " + ngayKhenThuong +
                ", Nội dung: " + noiDung + ", Mã nhân viên: " + maNhanVien + ", Tiền thưởng: " + tienThuong;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Khen thưởng cho nhân viên: " + maNhanVien + " - Tiêu đề: " + tieuDe;
    }
}
