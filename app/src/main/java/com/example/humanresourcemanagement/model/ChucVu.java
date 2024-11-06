package com.example.humanresourcemanagement.model;

public class ChucVu {
    private String chucvu_id;
    private String hschucvu;
    private String loaichucvu;

    // Constructor không tham số
    public ChucVu() {
    }

    // Constructor có tham số
    public ChucVu(String chucvu_id, String hschucvu, String loaichucvu) {
        this.chucvu_id = chucvu_id;
        this.hschucvu = hschucvu;
        this.loaichucvu = loaichucvu;
    }

    // Getter và Setter cho từng thuộc tính
    public String getChucvu_id() {
        return chucvu_id;
    }

    public void setChucvu_id(String chucvu_id) {
        this.chucvu_id = chucvu_id;
    }

    public String getHschucvu() {
        return hschucvu;
    }

    public void setHschucvu(String hschucvu) {
        this.hschucvu = hschucvu;
    }

    public String getLoaichucvu() {
        return loaichucvu;
    }

    public void setLoaichucvu(String loaichucvu) {
        this.loaichucvu = loaichucvu;
    }

    // Optional: Hàm toString() để dễ dàng hiển thị thông tin của ChucVu
    @Override
    public String toString() {
        return "ChucVu{" +
                "chucvu_id='" + chucvu_id + '\'' +
                ", hschucvu='" + hschucvu + '\'' +
                ", loaichucvu='" + loaichucvu + '\'' +
                '}';
    }
}

