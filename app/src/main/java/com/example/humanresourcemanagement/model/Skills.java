package com.example.humanresourcemanagement.model;

public class Skills {
    private String mask;
    private String tensk;

    public Skills() {
    }

    public Skills(String mask, String tensk) {
        this.mask = mask;
        this.tensk = tensk;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getTensk() {
        return tensk;
    }

    public void setTensk(String tensk) {
        this.tensk = tensk;
    }
}
