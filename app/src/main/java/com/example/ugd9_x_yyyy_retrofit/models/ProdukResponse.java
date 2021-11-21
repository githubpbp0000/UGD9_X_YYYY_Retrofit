package com.example.ugd9_x_yyyy_retrofit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProdukResponse {

    private String message;

    @SerializedName("produk")
    private List<Produk> produkList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Produk> getProdukList() {
        return produkList;
    }

    public void setProdukList(List<Produk> produkList) {
        this.produkList = produkList;
    }
}
