package com.example.quanlydoanvat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.adapter.ProductAdapter;
import com.example.quanlydoanvat.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList, filteredList;
    private Spinner spinnerCategory;
    private FloatingActionButton fabAddProduct;
    private Button btnDangXuat;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final int REQUEST_CODE_ADD = 1;
    private List<String> loaiSanPhamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

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

        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        productAdapter = new ProductAdapter(filteredList, this::onProductClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // **Danh sách loại sản phẩm**
        loaiSanPhamList = new ArrayList<>();
        loaiSanPhamList.add("Tất cả");
        loaiSanPhamList.add("Đồ ăn vặt");
        loaiSanPhamList.add("Đồ ngọt");
        loaiSanPhamList.add("Đồ cay");
        loaiSanPhamList.add("Đồ mặn");
        loaiSanPhamList.add("Nước uống");

        // **Gán danh sách vào Spinner**
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiSanPhamList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        // **Lắng nghe sự kiện chọn loại sản phẩm**
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

        // **Tải danh sách sản phẩm từ Firebase**
        loadProductsFromFirebase();

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

        btnDangXuat.setOnClickListener(v -> logoutUser());
    }

    /**
     * **Tải danh sách sản phẩm từ Firebase**
     */
    private void loadProductsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                filterProducts(spinnerCategory.getSelectedItem().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductListActivity.this, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseData", "Lỗi: " + error.getMessage());
            }
        });
    }

    /**
     * **Lọc danh sách sản phẩm theo loại**
     */
    private void filterProducts(String category) {
        filteredList.clear();
        if (category.equals("Tất cả")) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getLoaiSP().equalsIgnoreCase(category)) {
                    filteredList.add(product);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    private void onProductClick(Product product) {
        if (product == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ProductListActivity.this, AddEditActivity.class);
        intent.putExtra("id", product.getId());
        intent.putExtra("tenSP", product.getTenSP());
        intent.putExtra("loaiSP", product.getLoaiSP());
        intent.putExtra("giaSP", product.getGiaSP());
        intent.putExtra("ngaySX", product.getNgaySX());
        intent.putExtra("hanSD", product.getHanSD());
        intent.putExtra("soLuong", product.getSoLuong());
        startActivity(intent);
    }

    private void logoutUser() {
        mAuth.signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
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
                    filterProducts(spinnerCategory.getSelectedItem().toString());
                }
            } else {
                Toast.makeText(this, "Dữ liệu không hợp lệ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
