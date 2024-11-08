package com.example.humanresourcemanagement.model;

public class ChiTietSkill {
    private int id;
    private String employeeId;
    private String mask;
    private String moTa;

    // Constructor
    public ChiTietSkill(int id, String employeeId, String mask, String moTa) {
        this.id = id;
        this.employeeId = employeeId;
        this.mask = mask;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    // Phương thức trả về chi tiết kỹ năng
    public String getChiTietSkillDetails() {
        return "ID: " + id + ", Mã nhân viên: " + employeeId + ", Mã kỹ năng: " + mask + ", Mô tả: " + moTa;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Kỹ năng ID: " + mask + " - Nhân viên: " + employeeId;
    }
}
