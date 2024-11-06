package com.example.humanresourcemanagement.model;

public class BangCap {
    private int bangcap_id;
    private String tenBang;

    // Constructor
    public BangCap(int bangcapId, String tenBang) {
        this.bangcap_id = bangcapId;
        this.tenBang = tenBang;
    }

    public BangCap() {
    }

    // Getter và Setter cho bangcapId
    public int getBangcapId() {
        return bangcap_id;
    }

    public void setBangcapId(int bangcapId) {
        this.bangcap_id = bangcapId;
    }

    // Getter và Setter cho tenBang
    public String getTenBang() {
        return tenBang;
    }

    public void setTenBang(String tenBang) {
        this.tenBang = tenBang;
    }

    // Phương thức trả về chi tiết bằng cấp
    public String getBangCapDetails() {
        return "Bằng cấp ID: " + bangcap_id + ", Tên bằng: " + tenBang;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Bằng cấp: " + tenBang;
    }
}
