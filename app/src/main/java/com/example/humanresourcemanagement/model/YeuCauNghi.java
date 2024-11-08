package com.example.humanresourcemanagement.model;

public class YeuCauNghi {
    private String maNghi;
    private String maNhanVien;
    private String ngayBatDau;
    private String ngayKetThuc;
    private String loaiNghi;
    private String trangThai;
    private String lyDo;

    // Constructor
    public YeuCauNghi(String maNghi, String maNhanVien, String ngayBatDau, String ngayKetThuc, String loaiNghi, String trangThai, String lyDo) {
        this.maNghi = maNghi;
        this.maNhanVien = maNhanVien;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.loaiNghi = loaiNghi;
        this.trangThai = trangThai != null ? trangThai : "Đang chờ"; // Mặc định là "Đang chờ"
        this.lyDo = lyDo != null ? lyDo : ""; // Mặc định là chuỗi rỗng nếu không có lý do
    }

    public YeuCauNghi() {
    }

    // Getters và Setters cho các thuộc tính
    public String getMaNghi() {
        return maNghi;
    }

    public void setMaNghi(String maNghi) {
        this.maNghi = maNghi;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getLoaiNghi() {
        return loaiNghi;
    }

    public void setLoaiNghi(String loaiNghi) {
        this.loaiNghi = loaiNghi;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    // Phương thức trả về chi tiết yêu cầu nghỉ
    public String getYeuCauNghiDetails() {
        return "Mã nghỉ: " + maNghi + ", Mã nhân viên: " + maNhanVien + ", Ngày bắt đầu: " + ngayBatDau +
                ", Ngày kết thúc: " + ngayKetThuc + ", Loại nghỉ: " + loaiNghi + ", Trạng thái: " + trangThai +
                ", Lý do: " + lyDo;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Yêu cầu nghỉ của nhân viên: " + maNhanVien + " - Trạng thái: " + trangThai;
    }
}
