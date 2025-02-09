package com.example.quanlydoanvat.model;

public class Product {
    private String tenSP;
    private String loaiSP;
    private double giaSP;       // Sửa kiểu dữ liệu từ String -> double
    private String ngaySX;
    private String hanSD;
    private int soLuong;

    // Constructor
    public Product(String tenSP, String loaiSP, double giaSP, String ngaySX, String hanSD, int soLuong) {
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.giaSP = giaSP;
        this.ngaySX = ngaySX;
        this.hanSD = hanSD;
        this.soLuong = soLuong;
    }

    // Getter và Setter
    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public double getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(double giaSP) {
        this.giaSP = giaSP;
    }

    public String getNgaySX() {
        return ngaySX;
    }

    public void setNgaySX(String ngaySX) {
        this.ngaySX = ngaySX;
    }

    public String getHanSD() {
        return hanSD;
    }

    public void setHanSD(String hanSD) {
        this.hanSD = hanSD;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
