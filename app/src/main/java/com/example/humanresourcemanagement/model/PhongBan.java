package com.example.humanresourcemanagement.model;
public class PhongBan {
    private String maPhongBan;
    private String maQuanLy;
    private String tenPhongBan;


    public PhongBan() {
    }

    public PhongBan(String maPhongBan, String maQuanLy, String tenPhongBan) {
        this.maPhongBan = maPhongBan;
        this.maQuanLy = maQuanLy;
        this.tenPhongBan = tenPhongBan;
    }

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getMaQuanLy() {
        return maQuanLy;
    }

    public void setMaQuanLy(String maQuanLy) {
        this.maQuanLy = maQuanLy;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    @Override
    public String toString() {
        return "PhongBan{" +
                "maPhongBan='" + maPhongBan + '\'' +
                ", maQuanLy='" + maQuanLy + '\'' +
                ", tenPhongBan='" + tenPhongBan + '\'' +
                '}';
    }
}