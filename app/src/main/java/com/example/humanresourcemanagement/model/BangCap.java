package com.example.humanresourcemanagement.model;

public class BangCap {
    private String bangcap_id;
    private String tenBang;

    public BangCap() {
    }

    public BangCap(String bangcap_id, String tenBang) {
        this.bangcap_id = bangcap_id;
        this.tenBang = tenBang;
    }

    public String getBangcap_id() {
        return bangcap_id;
    }

    public void setBangcap_id(String bangcap_id) {
        this.bangcap_id = bangcap_id;
    }

    public String getTenBang() {
        return tenBang;
    }

    public void setTenBang(String tenBang) {
        this.tenBang = tenBang;
    }
}
