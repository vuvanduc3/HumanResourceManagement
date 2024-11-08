package com.example.humanresourcemanagement.model;

public class KyLuat {
    private int id;
    private String tenKyLuat;
    private String mucDoKyLuat;

    // Constructor
    public KyLuat(int id, String tenKyLuat, String mucDoKyLuat) {
        this.id = id;
        this.tenKyLuat = tenKyLuat;
        this.mucDoKyLuat = mucDoKyLuat;
    }

    public KyLuat() {
    }

    // Getters và Setters cho các thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenKyLuat() {
        return tenKyLuat;
    }

    public void setTenKyLuat(String tenKyLuat) {
        this.tenKyLuat = tenKyLuat;
    }

    public String getMucDoKyLuat() {
        return mucDoKyLuat;
    }

    public void setMucDoKyLuat(String mucDoKyLuat) {
        this.mucDoKyLuat = mucDoKyLuat;
    }

    // Phương thức trả về chi tiết kỷ luật
    public String getKyLuatDetails() {
        return "ID: " + id + ", Tên kỷ luật: " + tenKyLuat + ", Mức độ kỷ luật: " + mucDoKyLuat;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Kỷ luật: " + tenKyLuat + " - Mức độ: " + mucDoKyLuat;
    }
}
