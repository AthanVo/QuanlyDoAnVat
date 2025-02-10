package com.example.quanlydoanvat.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String tenSP, loaiSP, ngaySX, hanSD;
    private double giaSP;
    private int soLuong;

    // **Constructor rỗng (Bắt buộc cho Firebase)**
    public Product() {
    }

    // **Constructor đầy đủ có id (Dùng khi chỉnh sửa sản phẩm)**
    public Product(String id, String tenSP, String loaiSP, double giaSP, String ngaySX, String hanSD, int soLuong) {
        this.id = id;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.giaSP = giaSP;
        this.ngaySX = ngaySX;
        this.hanSD = hanSD;
        this.soLuong = soLuong;
    }

    // **Constructor không có id (Dùng khi thêm sản phẩm mới)**
    public Product(String tenSP, String loaiSP, double giaSP, String ngaySX, String hanSD, int soLuong) {
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.giaSP = giaSP;
        this.ngaySX = ngaySX;
        this.hanSD = hanSD;
        this.soLuong = soLuong;
    }

    // Getter & Setter
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public String getTenSP()
    {
        return tenSP;
    }
    public void setTenSP(String tenSP)
    {
        this.tenSP = tenSP;
    }

    public String getLoaiSP()
    {
        return loaiSP;
    }
    public void setLoaiSP(String loaiSP)
    {
        this.loaiSP = loaiSP;
    }

    public double getGiaSP()
    {
        return giaSP;
    }
    public void setGiaSP(double giaSP)
    {
        this.giaSP = giaSP;
    }

    public String getNgaySX()
    {
        return ngaySX;
    }
    public void setNgaySX(String ngaySX)
    {
        this.ngaySX = ngaySX;
    }

    public String getHanSD()
    {
        return hanSD;
    }
    public void setHanSD(String hanSD)
    {
        this.hanSD = hanSD;
    }

    public int getSoLuong()
    {
        return soLuong;
    }
    public void setSoLuong(int soLuong)
    {
        this.soLuong = soLuong;
    }
}
