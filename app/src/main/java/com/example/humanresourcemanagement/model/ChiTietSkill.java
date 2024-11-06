package com.example.humanresourcemanagement.model;

public class ChiTietSkill {
    private int id;
    private String maNhanVien;
    private String maSkill;
    private String moTa;

    // Constructor
    public ChiTietSkill(int id, String maNhanVien, String maSkill, String moTa) {
        this.id = id;
        this.maNhanVien = maNhanVien;
        this.maSkill = maSkill;
        this.moTa = moTa;
    }

    public ChiTietSkill() {
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

    public String getMaSkill() {
        return maSkill;
    }

    public void setMaSkill(String maSkill) {
        this.maSkill = maSkill;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    // Phương thức trả về chi tiết kỹ năng
    public String getChiTietSkillDetails() {
        return "ID: " + id + ", Mã nhân viên: " + maNhanVien + ", Mã kỹ năng: " + maSkill + ", Mô tả: " + moTa;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Kỹ năng ID: " + maSkill + " - Nhân viên: " + maNhanVien;
    }
}
