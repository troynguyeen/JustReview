package com.example.justreview;

public class Sach {
    private int anh;
    private String TenSach;

    public Sach(){

    }
    public Sach(int Anh,String tenSach){
        this.anh=Anh;
        this.TenSach=tenSach;
    }

    public int getAnh() {
        return anh;
    }

    public void setAnh(int anh) {
        this.anh = anh;
    }

    public String getTenSach() {
        return TenSach;
    }

    public void setTenSach(String tenSach) {
        TenSach = tenSach;
    }
}
