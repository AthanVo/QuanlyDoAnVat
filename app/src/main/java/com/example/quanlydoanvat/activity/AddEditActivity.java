package com.example.quanlydoanvat.activity;

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
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Ánh xạ các EditText với ID trong XML
        edtTenSP = findViewById(R.id.edtTenSP);
        edtLoaiSP = findViewById(R.id.edtLoaiSP);   // Thêm dòng này để tránh lỗi NullPointerException
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtNgaySX = findViewById(R.id.edtNgaySX);
        edtHanSD = findViewById(R.id.edtHanSD);
        edtSoLuong = findViewById(R.id.edtSoLuong);


        // Xử lý sự kiện khi nhấn nút Lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void saveProduct() {
        // Lấy dữ liệu từ các ô nhập liệu
        String tenSP = edtTenSP.getText().toString().trim();
        String loaiSP = edtLoaiSP.getText().toString().trim();
        String giaSP = edtGiaSP.getText().toString().trim();
        String ngaySX = edtNgaySX.getText().toString().trim();
        String hanSD = edtHanSD.getText().toString().trim();
        String soLuong = edtSoLuong.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(tenSP) || TextUtils.isEmpty(loaiSP) ||
                TextUtils.isEmpty(giaSP) || TextUtils.isEmpty(ngaySX) ||
                TextUtils.isEmpty(hanSD) || TextUtils.isEmpty(soLuong)) {

            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double gia = Double.parseDouble(giaSP);
            int sl = Integer.parseInt(soLuong);

            // Tạo đối tượng Product với dữ liệu đã nhập
            Product product = new Product(tenSP, loaiSP, gia, ngaySX, hanSD, sl);

            Toast.makeText(this, "Đã lưu sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity sau khi lưu thành công

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
}
