package com.example.ugd9_x_yyyy_retrofit.api;

import com.example.ugd9_x_yyyy_retrofit.models.Produk;
import com.example.ugd9_x_yyyy_retrofit.models.ProdukResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @Headers({"Accept: application/json"})
    @GET("produk")
    Call<ProdukResponse> getAllProduk();

    @Headers({"Accept: application/json"})
    @GET("produk/{id}")
    Call<ProdukResponse> getProdukById(@Path("id") long id);

    @Headers({"Accept: application/json"})
    @POST("produk")
    Call<ProdukResponse> createProduk(@Body Produk produk);

    @Headers({"Accept: application/json"})
    @PUT("produk/{id}")
    Call<ProdukResponse> updateProduk(@Path("id") long id,
                                      @Body Produk produk);

    @Headers({"Accept: application/json"})
    @DELETE("produk/{id}")
    Call<ProdukResponse> deleteProduk(@Path("id") long id);
}

