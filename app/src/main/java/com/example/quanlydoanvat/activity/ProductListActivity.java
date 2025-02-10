package com.example.quanlydoanvat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.adapter.ProductAdapter;
import com.example.quanlydoanvat.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Spinner spinnerCategory;
    private FloatingActionButton fabAddProduct;
    private Button btnDangXuat; // Logout button
    private FirebaseAuth mAuth;
    private static final int REQUEST_CODE_ADD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Kiểm tra trạng thái đăng nhập
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProductListActivity.this, MainActivity.class));
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewProducts);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        btnDangXuat = findViewById(R.id.btnDangXuat);

        // Dữ liệu mẫu (có thể thay bằng Firebase hoặc Database)
        productList = new ArrayList<>();
        productList.add(new Product("Snack Khoai Tây", "Đồ ăn vặt", 25000, "01/01/2024", "01/01/2025", 20));
        productList.add(new Product("Bim Bim Tôm Cay", "Đồ ăn mặn", 18000, "15/12/2023", "15/06/2024", 35));
        productList.add(new Product("Kẹo Dẻo Trái Cây", "Đồ ngọt", 30000, "20/11/2023", "20/05/2024", 15));

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList, this::onProductClick);
        recyclerView.setAdapter(productAdapter);

        // Setup Spinner (Dropdown)
        List<String> categories = new ArrayList<>();
        categories.add("Tất cả");
        categories.add("Đồ ăn vặt");
        categories.add("Đồ ăn mặn");
        categories.add("Đồ ngọt");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterProducts(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Thêm sản phẩm mới
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

        // Đăng xuất
        btnDangXuat.setOnClickListener(v -> logoutUser());
    }

    private void filterProducts(String category) {
        if (productList == null) {
            return;  // Tránh lỗi NullPointerException
        }

        List<Product> filteredList = new ArrayList<>();
        if (category.equals("Tất cả")) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getLoaiSP().equalsIgnoreCase(category)) {
                    filteredList.add(product);
                }
            }
        }
        productAdapter.updateProductList(filteredList);
    }

    private void onProductClick(Product product) {
        if (product == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ProductListActivity.this, AddEditActivity.class);
        intent.putExtra("product", product);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    private void logoutUser() {
        mAuth.signOut();

        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();  // Dùng commit() thay vì apply()

        Toast.makeText(ProductListActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProductListActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("newProduct")) {
                Product newProduct = (Product) data.getSerializableExtra("newProduct");

                if (newProduct != null) {
                    productList.add(newProduct);
                    productAdapter.notifyDataSetChanged();

                    // Refresh list based on selected category
                    String selectedCategory = spinnerCategory.getSelectedItem().toString();
                    filterProducts(selectedCategory);
                }
            } else {
                Toast.makeText(this, "Dữ liệu không hợp lệ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
