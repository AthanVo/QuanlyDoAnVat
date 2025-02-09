package com.example.quanlydoanvat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.model.Product;

public class AddProductActivity extends AppCompatActivity {

    EditText edtTenSP, edtLoaiSP, edtGiaSP, edtNgaySX, edtHanSD, edtSoLuong;
    Button btnLuu, btnTroLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtTenSP = findViewById(R.id.edtTenSP);
        edtLoaiSP = findViewById(R.id.edtLoaiSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtNgaySX = findViewById(R.id.edtNgaySX);
        edtHanSD = findViewById(R.id.edtHanSD);
        edtSoLuong = findViewById(R.id.edtSoLuong);

        btnLuu = findViewById(R.id.btnLuu);
        btnTroLai = findViewById(R.id.btnTroLai);

        // Xử lý sự kiện lưu sản phẩm
        btnLuu.setOnClickListener(v -> saveProduct());

        // Xử lý sự kiện quay lại
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

            Product newProduct = new Product(tenSP, loaiSP, gia, ngaySX, hanSD, sl);

            // Truyền dữ liệu sản phẩm mới về ProductListActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newProduct", newProduct);
            setResult(Activity.RESULT_OK, resultIntent);

            Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
}
