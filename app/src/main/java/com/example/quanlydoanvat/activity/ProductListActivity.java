package com.example.quanlydoanvat.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.adapter.ProductAdapter;
import com.example.quanlydoanvat.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        // Dữ liệu mẫu để hiển thị
        productList.add(new Product("Snack Khoai Tây", "Đồ ăn vặt", 25000, "01/01/2024", "01/01/2025", 50));
        productList.add(new Product("Bánh Gạo", "Đồ ăn vặt", 15000, "02/01/2024", "02/01/2025", 30));
        productList.add(new Product("Kẹo Socola", "Kẹo", 20000, "03/01/2024", "03/01/2025", 40));

        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);
    }
}
