package com.example.humanresourcemanagement.model;

public class ThongBao {
    private String maThongBao;
    private String maNhanVien;
    private String loaiThongBao;
    private String thongDiep;
    private String ngayThongBao;
    private String trangThai;

    // Constructor
    public ThongBao(String maThongBao, String maNhanVien, String loaiThongBao, String thongDiep, String ngayThongBao, String trangThai) {
        this.maThongBao = maThongBao;
        this.maNhanVien = maNhanVien;
        this.loaiThongBao = loaiThongBao;
        this.thongDiep = thongDiep;
        this.ngayThongBao = ngayThongBao;
        this.trangThai = trangThai != null ? trangThai : "Chưa đọc"; // Mặc định là "Chưa đọc"
    }

    public ThongBao() {
    }

    // Getters và Setters cho các thuộc tính
    public String getMaThongBao() {
        return maThongBao;
    }

    public void setMaThongBao(String maThongBao) {
        this.maThongBao = maThongBao;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getLoaiThongBao() {
        return loaiThongBao;
    }

    public void setLoaiThongBao(String loaiThongBao) {
        this.loaiThongBao = loaiThongBao;
    }

    public String getThongDiep() {
        return thongDiep;
    }

    public void setThongDiep(String thongDiep) {
        this.thongDiep = thongDiep;
    }

    public String getNgayThongBao() {
        return ngayThongBao;
    }

    public void setNgayThongBao(String ngayThongBao) {
        this.ngayThongBao = ngayThongBao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Phương thức trả về chi tiết thông báo
    public String getThongBaoDetails() {
        return "Mã thông báo: " + maThongBao + ", Mã nhân viên: " + maNhanVien + ", Loại thông báo: " + loaiThongBao +
                ", Thông điệp: " + thongDiep + ", Ngày thông báo: " + ngayThongBao + ", Trạng thái: " + trangThai;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Thông báo ID: " + maThongBao + " - Nhân viên: " + maNhanVien;
    }
}
