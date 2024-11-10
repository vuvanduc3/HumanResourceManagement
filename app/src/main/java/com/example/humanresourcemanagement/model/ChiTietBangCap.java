package com.example.humanresourcemanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChiTietBangCap implements Parcelable {
    private String bangcap_id; // Thay đổi từ int sang String để tương thích với Firebase
    private String employeeId; // employeeId trong Firebase
    private String imageUrl; // imageUrl trong Firebase
    private String mota; // mota trong Firebase
    private String ngaycap; // ngaycap trong Firebase
    private String xacthuc; // xacthuc trong Firebase

    // Constructor
    public ChiTietBangCap(String bangcap_id, String employeeId, String imageUrl, String mota, String ngaycap, String xacthuc) {
        this.bangcap_id = bangcap_id;
        this.employeeId = employeeId;
        this.imageUrl = imageUrl;
        this.mota = mota;
        this.ngaycap = ngaycap;
        this.xacthuc = xacthuc;
    }

    public ChiTietBangCap() {
    }

    // Getters và Setters cho các thuộc tính
    public String getBangcap_id() {
        return bangcap_id;
    }

    public void setBangcap_id(String bangcap_id) {
        this.bangcap_id = bangcap_id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getNgaycap() {
        return ngaycap;
    }

    public void setNgaycap(String ngaycap) {
        this.ngaycap = ngaycap;
    }

    public String getXacthuc() {
        return xacthuc;
    }

    public void setXacthuc(String xacthuc) {
        this.xacthuc = xacthuc;
    }

    // Phương thức trả về chi tiết bằng cấp
    public String getChiTietBangCapDetails() {
        return "Mã bằng cấp: " + bangcap_id + ", Mã nhân viên: " + employeeId + ", Ngày cấp: " + ngaycap +
                ", Xác thực: " + xacthuc + ", Mô tả: " + mota + ", Hình ảnh: " + imageUrl;
    }

    // Ghi đè phương thức toString
    @Override
    public String toString() {
        return "Bằng cấp ID: " + bangcap_id + " - Nhân viên: " + employeeId;
    }

    // Implement Parcelable
    protected ChiTietBangCap(Parcel in) {
        bangcap_id = in.readString();
        employeeId = in.readString();
        imageUrl = in.readString();
        mota = in.readString();
        ngaycap = in.readString();
        xacthuc = in.readString();
    }

    public static final Creator<ChiTietBangCap> CREATOR = new Creator<ChiTietBangCap>() {
        @Override
        public ChiTietBangCap createFromParcel(Parcel in) {
            return new ChiTietBangCap(in);
        }

        @Override
        public ChiTietBangCap[] newArray(int size) {
            return new ChiTietBangCap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bangcap_id);
        dest.writeString(employeeId);
        dest.writeString(imageUrl);
        dest.writeString(mota);
        dest.writeString(ngaycap);
        dest.writeString(xacthuc);
    }
}
