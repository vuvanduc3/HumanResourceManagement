package com.example.humanresourcemanagement.model;

import java.util.Date;

public class ChiTietBangCap {
    private int id;
    private String loaiBangCap;
    private Date ngayCap;
    private String maNhanVien;
    private String hinhAnh;
    private boolean xacThuc;
    private String moTa;

    // Constructor
    public ChiTietBangCap(int id, String loaiBangCap, Date ngayCap, String maNhanVien, String hinhAnh, boolean xacThuc, String moTa) {
        this.id = id;
        this.loaiBangCap = loaiBangCap;
        this.ngayCap = ngayCap;
        this.maNhanVien = maNhanVien;
        this.hinhAnh = hinhAnh;
        this.xacThuc = xacThuc;
        this.moTa = moTa;
    }

    public ChiTietBangCap() {
    }

    // Getters và Setters cho các thuộc tính
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoaiBangCap() {
        return loaiBangCap;
    }

    public void setLoaiBangCap(String loaiBangCap) {
        this.loaiBangCap = loaiBangCap;
    }

    public Date getNgayCap() {
        return ngayCap;
    }

    public void setNgayCap(Date ngayCap) {
        this.ngayCap = ngayCap;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public boolean isXacThuc() {
        return xacThuc;
    }

    public void setXacThuc(boolean xacThuc) {
        this.xacThuc = xacThuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    // Phương thức trả về chi tiết bằng cấp
    public String getChiTietBangCapDetails() {
        return "ID: " + id + ", Loại bằng cấp: " + loaiBangCap + ", Ngày cấp: " + ngayCap +
                ", Mã nhân viên: " + maNhanVien + ", Hình ảnh: " + hinhAnh +
                ", Xác thực: " + xacThuc + ", Mô tả: " + moTa;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Bằng cấp: " + loaiBangCap + " - Nhân viên: " + maNhanVien;
    }
}
