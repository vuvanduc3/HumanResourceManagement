package com.example.humanresourcemanagement.model;

public class ChucVu {
    private int chucvuId;
    private String loaichucvu;
    private double hschucvu;

    // Constructor
    public ChucVu(int chucvuId, String loaichucvu, double hschucvu) {
        this.chucvuId = chucvuId;
        this.loaichucvu = loaichucvu;
        this.hschucvu = hschucvu;
    }

    public ChucVu() {
    }

    // Getters và Setters cho các thuộc tính
    public int getChucvuId() {
        return chucvuId;
    }

    public void setChucvuId(int chucvuId) {
        this.chucvuId = chucvuId;
    }

    public String getLoaichucvu() {
        return loaichucvu;
    }

    public void setLoaichucvu(String loaichucvu) {
        this.loaichucvu = loaichucvu;
    }

    public double getHschucvu() {
        return hschucvu;
    }

    public void setHschucvu(double hschucvu) {
        this.hschucvu = hschucvu;
    }

    // Phương thức trả về chi tiết chức vụ
    public String getChucVuDetails() {
        return "ID: " + chucvuId + ", Loại chức vụ: " + loaichucvu + ", Hệ số chức vụ: " + hschucvu;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Chức vụ: " + loaichucvu + " (ID: " + chucvuId + ")";
    }
}
