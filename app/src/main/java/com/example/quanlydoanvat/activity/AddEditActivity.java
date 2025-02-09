package com.example.quanlydoanvat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.model.Product;

public class AddEditActivity extends AppCompatActivity {

    EditText edtTenSP, edtLoaiSP, edtGiaSP, edtNgaySX, edtHanSD, edtSoLuong;
    Button btnSave, btnDelete, btnTroLai;

    private Product currentProduct;  // Lưu sản phẩm hiện tại để xử lý

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        edtTenSP = findViewById(R.id.edtTenSP);
        edtLoaiSP = findViewById(R.id.edtLoaiSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtNgaySX = findViewById(R.id.edtNgaySX);
        edtHanSD = findViewById(R.id.edtHanSD);
        edtSoLuong = findViewById(R.id.edtSoLuong);

        btnSave = findViewById(R.id.btnLuu);
        btnDelete = findViewById(R.id.btnXoa);
        btnTroLai = findViewById(R.id.btnTroLai);

        // Nhận dữ liệu sản phẩm từ Intent
        if (getIntent() != null) {
            currentProduct = new Product(
                    getIntent().getStringExtra("tenSP"),
                    getIntent().getStringExtra("loaiSP"),
                    getIntent().getDoubleExtra("giaSP", 0),
                    getIntent().getStringExtra("ngaySX"),
                    getIntent().getStringExtra("hanSD"),
                    getIntent().getIntExtra("soLuong", 0)
            );

            // Hiển thị thông tin sản phẩm lên các EditText
            edtTenSP.setText(currentProduct.getTenSP());
            edtLoaiSP.setText(currentProduct.getLoaiSP());
            edtGiaSP.setText(String.valueOf(currentProduct.getGiaSP()));
            edtNgaySX.setText(currentProduct.getNgaySX());
            edtHanSD.setText(currentProduct.getHanSD());
            edtSoLuong.setText(String.valueOf(currentProduct.getSoLuong()));
        }

        // Xử lý sự kiện Lưu sản phẩm
        btnSave.setOnClickListener(v -> saveProduct());

        // Xử lý sự kiện Xóa sản phẩm
        btnDelete.setOnClickListener(v -> deleteProduct());

        // Xử lý sự kiện Quay lại
        btnTroLai.setOnClickListener(v -> finish());
    }

    private void saveProduct() {
        String tenSP = edtTenSP.getText().toString().trim();
        String loaiSP = edtLoaiSP.getText().toString().trim();
        String giaSP = edtGiaSP.getText().toString().trim();
        String ngaySX = edtNgaySX.getText().toString().trim();
        String hanSD = edtHanSD.getText().toString().trim();
        String soLuong = edtSoLuong.getText().toString().trim();

        if (TextUtils.isEmpty(tenSP) || TextUtils.isEmpty(loaiSP) ||
                TextUtils.isEmpty(giaSP) || TextUtils.isEmpty(ngaySX) ||
                TextUtils.isEmpty(hanSD) || TextUtils.isEmpty(soLuong)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double gia = Double.parseDouble(giaSP);
            int sl = Integer.parseInt(soLuong);

            // Kiểm tra xem có đang chỉnh sửa sản phẩm hay không
            if (currentProduct != null) {
                // Cập nhật thông tin sản phẩm hiện tại
                currentProduct.setTenSP(tenSP);
                currentProduct.setLoaiSP(loaiSP);
                currentProduct.setGiaSP(gia);
                currentProduct.setNgaySX(ngaySX);
                currentProduct.setHanSD(hanSD);
                currentProduct.setSoLuong(sl);
            } else {
                // Tạo sản phẩm mới nếu không có sản phẩm hiện tại
                currentProduct = new Product(tenSP, loaiSP, gia, ngaySX, hanSD, sl);
            }

            // Trả kết quả về ProductListActivity để cập nhật giao diện
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedProduct", currentProduct);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Lưu sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước đó
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteProduct() {
        // Giả lập xóa sản phẩm bằng cách trả kết quả về ProductListActivity
        Intent intent = new Intent();
        Product currentProduct = null;
        intent.putExtra("deleteProduct", currentProduct.getTenSP());
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
