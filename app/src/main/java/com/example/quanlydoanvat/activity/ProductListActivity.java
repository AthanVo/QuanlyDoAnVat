package com.example.quanlydoanvat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.adapter.ProductAdapter;
import com.example.quanlydoanvat.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Spinner spinnerCategory;

    private static final int REQUEST_CODE_EDIT = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // Khởi tạo danh sách sản phẩm mẫu
        productList = new ArrayList<>();
        productList.add(new Product("Snack Khoai Tây", "Đồ ăn vặt", 25000, "01/01/2024", "01/01/2025", 20));
        productList.add(new Product("Bim Bim Tôm Cay", "Đồ ăn mặn", 18000, "15/12/2023", "15/06/2024", 35));
        productList.add(new Product("Kẹo Dẻo Trái Cây", "Đồ ngọt", 30000, "20/11/2023", "20/05/2024", 15));

        // Cài đặt RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList, this::onProductClick);
        recyclerView.setAdapter(productAdapter);

        // Xử lý sự kiện chọn loại sản phẩm từ Spinner (lọc sản phẩm)
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterProducts(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không chọn
            }
        });
    }

    // Sự kiện khi click vào sản phẩm
    private void onProductClick(Product product) {
        Intent intent = new Intent(ProductListActivity.this, AddEditActivity.class);
        intent.putExtra("tenSP", product.getTenSP());
        intent.putExtra("loaiSP", product.getLoaiSP());
        intent.putExtra("giaSP", product.getGiaSP());
        intent.putExtra("ngaySX", product.getNgaySX());
        intent.putExtra("hanSD", product.getHanSD());
        intent.putExtra("soLuong", product.getSoLuong());
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    // Lọc sản phẩm theo loại
    private void filterProducts(String category) {
        List<Product> filteredList = new ArrayList<>();
        if (category.equals("Tất cả")) {
            filteredList = productList;
        } else {
            for (Product product : productList) {
                if (product.getLoaiSP().equalsIgnoreCase(category)) {
                    filteredList.add(product);
                }
            }
        }
        productAdapter.updateProductList(filteredList);
    }

    // Xử lý kết quả trả về sau khi chỉnh sửa hoặc xóa sản phẩm
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            // Kiểm tra xem người dùng đã xóa sản phẩm hay lưu sản phẩm
            String deletedProductName = data.getStringExtra("deleteProduct");
            if (deletedProductName != null) {
                deleteProductByName(deletedProductName);
            } else {
                Product updatedProduct = (Product) data.getSerializableExtra("updatedProduct");
                if (updatedProduct != null) {
                    updateProduct(updatedProduct);
                }
            }
        }
    }

    // Hàm cập nhật sản phẩm trong danh sách
    private void updateProduct(Product updatedProduct) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getTenSP().equals(updatedProduct.getTenSP())) {
                productList.set(i, updatedProduct);
                productAdapter.notifyItemChanged(i);
                Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    // Hàm xóa sản phẩm trong danh sách
    private void deleteProductByName(String productName) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getTenSP().equals(productName)) {
                productList.remove(i);
                productAdapter.notifyItemRemoved(i);
                Toast.makeText(this, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
}
