package com.example.humanresourcemanagement.model;

public class Employee {
    private String cccd;
    private String chucvuId;
    private String diachi;
    private String employeeId;
    private String gioitinh;
    private String id;
    private String imageUrl;
    private String luongcoban;
    private String matKhau;
    private String name;
    private String ngaybatdau;
    private String ngaysinh;
    private String phongbanId;
    private String sdt;
    private String trangthai;

    public Employee() {
        // Bắt buộc để Firebase tự động chuyển đổi dữ liệu
    }

    public Employee(String cccd, String chucvuId, String diachi, String employeeId, String gioitinh,
                    String id, String luongcoban, String matKhau, String name,
                    String ngaybatdau, String ngaysinh, String phongbanId, String sdt, String trangthai) {
        this.cccd = cccd;
        this.chucvuId = chucvuId;
        this.diachi = diachi;
        this.employeeId = employeeId;
        this.gioitinh = gioitinh;
        this.id = id;
        this.imageUrl = imageUrl;
        this.luongcoban = luongcoban;
        this.matKhau = matKhau;
        this.name = name;
        this.ngaybatdau = ngaybatdau;
        this.ngaysinh = ngaysinh;
        this.phongbanId = phongbanId;
        this.sdt = sdt;
        this.trangthai = trangthai;
    }

    // Getter methods
    public String getCccd() { return cccd; }
    public String getChucvuId() { return chucvuId; }
    public String getDiachi() { return diachi; }
    public String getEmployeeId() { return employeeId; }
    public String getGioitinh() { return gioitinh; }
    public String getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public String getLuongcoban() { return luongcoban; }
    public String getMatKhau() { return matKhau; }
    public String getName() { return name; }
    public String getNgaybatdau() { return ngaybatdau; }
    public String getNgaysinh() { return ngaysinh; }
    public String getPhongbanId() { return phongbanId; }
    public String getSdt() { return sdt; }
    public String getTrangthai() { return trangthai; }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setChucvuId(String chucvuId) {
        this.chucvuId = chucvuId;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLuongcoban(String luongcoban) {
        this.luongcoban = luongcoban;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNgaybatdau(String ngaybatdau) {
        this.ngaybatdau = ngaybatdau;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public void setPhongbanId(String phongbanId) {
        this.phongbanId = phongbanId;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}

