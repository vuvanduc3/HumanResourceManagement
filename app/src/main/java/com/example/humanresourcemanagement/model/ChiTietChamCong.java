package com.example.humanresourcemanagement.model;

import java.util.Date;

public class ChiTietChamCong {
    private String maLuongThang;
    private String maChamCong;
    private String maNhanVien;
    private Date ngayLamViec;
    private String gioVao;
    private String gioRa;
    private String gioTangCa;
    private boolean diMuon;
    private boolean vangMat;
    private String loaiNghi;

    // Constructor
    public ChiTietChamCong(String maLuongThang, String maChamCong, String maNhanVien, Date ngayLamViec, String gioVao, String gioRa, String gioTangCa, boolean diMuon, boolean vangMat, String loaiNghi) {
        this.maLuongThang = maLuongThang;
        this.maChamCong = maChamCong;
        this.maNhanVien = maNhanVien;
        this.ngayLamViec = ngayLamViec;
        this.gioVao = gioVao;
        this.gioRa = gioRa;
        this.gioTangCa = gioTangCa;
        this.diMuon = diMuon;
        this.vangMat = vangMat;
        this.loaiNghi = loaiNghi != null ? loaiNghi : "Không";
    }

    public ChiTietChamCong() {
    }

    // Getters và Setters cho các thuộc tính
    public String getMaLuongThang() {
        return maLuongThang;
    }

    public void setMaLuongThang(String maLuongThang) {
        this.maLuongThang = maLuongThang;
    }

    public String getMaChamCong() {
        return maChamCong;
    }

    public void setMaChamCong(String maChamCong) {
        this.maChamCong = maChamCong;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Date getNgayLamViec() {
        return ngayLamViec;
    }

    public void setNgayLamViec(Date ngayLamViec) {
        this.ngayLamViec = ngayLamViec;
    }

    public String getGioVao() {
        return gioVao;
    }

    public void setGioVao(String gioVao) {
        this.gioVao = gioVao;
    }

    public String getGioRa() {
        return gioRa;
    }

    public void setGioRa(String gioRa) {
        this.gioRa = gioRa;
    }

    public String getGioTangCa() {
        return gioTangCa;
    }

    public void setGioTangCa(String gioTangCa) {
        this.gioTangCa = gioTangCa;
    }

    public boolean isDiMuon() {
        return diMuon;
    }

    public void setDiMuon(boolean diMuon) {
        this.diMuon = diMuon;
    }

    public boolean isVangMat() {
        return vangMat;
    }

    public void setVangMat(boolean vangMat) {
        this.vangMat = vangMat;
    }

    public String getLoaiNghi() {
        return loaiNghi;
    }

    public void setLoaiNghi(String loaiNghi) {
        this.loaiNghi = loaiNghi;
    }

    // Phương thức trả về chi tiết chấm công
    public String getChiTietChamCongDetails() {
        return "Mã lương tháng: " + maLuongThang + ", Mã chấm công: " + maChamCong + ", Mã nhân viên: " + maNhanVien +
                ", Ngày làm việc: " + ngayLamViec + ", Giờ vào: " + gioVao + ", Giờ ra: " + gioRa +
                ", Giờ tăng ca: " + gioTangCa + ", Đi muộn: " + diMuon + ", Vắng mặt: " + vangMat +
                ", Loại nghỉ: " + loaiNghi;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Chấm công ID: " + maChamCong + " - Nhân viên: " + maNhanVien;
    }
}
