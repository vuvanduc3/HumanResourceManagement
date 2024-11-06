package com.example.humanresourcemanagement.model;

public class PhongBan {
    private String maPhongBan;
    private String tenPhongBan;
    private String maQuanLy;

    // Constructor
    public PhongBan(String maPhongBan, String tenPhongBan, String maQuanLy) {
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.maQuanLy = maQuanLy;
    }

    public PhongBan() {
    }

    // Getters và Setters cho các thuộc tính
    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public String getMaQuanLy() {
        return maQuanLy;
    }

    public void setMaQuanLy(String maQuanLy) {
        this.maQuanLy = maQuanLy;
    }

    // Phương thức trả về chi tiết phòng ban
    public String getPhongBanDetails() {
        return "Mã phòng ban: " + maPhongBan + ", Tên phòng ban: " + tenPhongBan + ", Mã quản lý: " + maQuanLy;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Phòng ban: " + tenPhongBan + " (ID: " + maPhongBan + ")";
    }
}
