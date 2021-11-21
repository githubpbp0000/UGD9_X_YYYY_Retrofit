package com.example.ugd9_x_yyyy_retrofit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ugd9_x_yyyy_retrofit.adapters.ProdukAdapter;
import com.example.ugd9_x_yyyy_retrofit.api.ApiClient;
import com.example.ugd9_x_yyyy_retrofit.api.ApiInterface;
import com.example.ugd9_x_yyyy_retrofit.models.ProdukResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int LAUNCH_ADD_ACTIVITY = 123;

    private SwipeRefreshLayout srProduk;
    private ProdukAdapter adapter;
    private SearchView svProduk;
    private LinearLayout layoutLoading;

    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        layoutLoading = findViewById(R.id.layout_loading);
        srProduk = findViewById(R.id.sr_produk);
        svProduk = findViewById(R.id.sv_produk);

        srProduk.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllProduk();
            }
        });

        svProduk.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(i, LAUNCH_ADD_ACTIVITY);
            }
        });

        RecyclerView rvProduk = findViewById(R.id.rv_produk);
        adapter = new ProdukAdapter(new ArrayList<>(), this);
        int orientation = getResources().getConfiguration().orientation;

        int spanCount = orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        rvProduk.setLayoutManager(new GridLayoutManager(MainActivity.this, spanCount));

        rvProduk.setAdapter(adapter);

        getAllProduk();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == Activity.RESULT_OK)
            getAllProduk();
    }

    private void getAllProduk() {
        Call<ProdukResponse> call = apiService.getAllProduk();

        srProduk.setRefreshing(true);

        call.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call,
                                   Response<ProdukResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setProdukList(response.body().getProdukList());
                    adapter.getFilter().filter(svProduk.getQuery());

                    Toast.makeText(MainActivity.this,
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this,
                                jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                srProduk.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error",
                        Toast.LENGTH_SHORT).show();
                srProduk.setRefreshing(false);
            }
        });
    }

    public void deleteProduk(long id) {
        Call<ProdukResponse> call = apiService.deleteProduk(id);

        setLoading(true);

        call.enqueue(new Callback<ProdukResponse>() {
            @Override
            public void onResponse(Call<ProdukResponse> call,
                                   Response<ProdukResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this,
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getAllProduk();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this,
                                jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                setLoading(false);
            }

            @Override
            public void onFailure(Call<ProdukResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error",
                        Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
    }

    // Fungsi untuk menampilkan layout loading
    private void setLoading(boolean isLoading) {
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.GONE);
        }
    }
}