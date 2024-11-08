package com.example.humanresourcemanagement.model;
import com.google.firebase.firestore.PropertyName;

public class CCCD {
    private String cccdNumber;

    @PropertyName("frontImage")
    private String frontImage;

    @PropertyName("backImage")
    private String backImage;

    // Getter và Setter cho các trường
    public String getCccdNumber() {
        return cccdNumber;
    }

    public void setCccdNumber(String cccdNumber) {
        this.cccdNumber = cccdNumber;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    @Override
    public String toString() {
        return "CCCD{" +
                "cccdNumber='" + cccdNumber + '\'' +
                ", frontImage='" + frontImage + '\'' +
                ", backImage='" + backImage + '\'' +
                '}';
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }
}
