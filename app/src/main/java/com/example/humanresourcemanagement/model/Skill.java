package com.example.humanresourcemanagement.model;

public class Skill {
    private String id;
    private String tenSkill;

    // Constructor
    public Skill(String id, String tenSkill) {
        this.id = id;
        this.tenSkill = tenSkill;
    }

    public Skill() {
    }

    // Getters và Setters cho các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenSkill() {
        return tenSkill;
    }

    public void setTenSkill(String tenSkill) {
        this.tenSkill = tenSkill;
    }

    // Phương thức trả về chi tiết kỹ năng
    public String getSkillDetails() {
        return "ID: " + id + ", Tên kỹ năng: " + tenSkill;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Kỹ năng: " + tenSkill;
    }
}
