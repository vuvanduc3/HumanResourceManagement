package com.example.humanresourcemanagement.model;

public class Luong {
    private double luong;
    private double phucap;

    // Constructor
    public Luong(double luong, double phucap) {
        this.luong = luong;
        this.phucap = phucap;
    }

    public Luong() {
    }

    // Getters và Setters cho các thuộc tính
    public double getLuong() {
        return luong;
    }

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public double getPhucap() {
        return phucap;
    }

    public void setPhucap(double phucap) {
        this.phucap = phucap;
    }

    // Phương thức trả về chi tiết lương
    public String getLuongDetails() {
        return "Lương: " + luong + ", Phụ cấp: " + phucap;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Lương: " + luong + ", Phụ cấp: " + phucap;
    }
}
