package com.example.quanlydoanvat.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.model.Product;

import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    EditText edtTenSP, edtLoaiSP, edtGiaSP, edtSoLuong;
    private TextView tvNgaySX, tvHanSD;
    Button btnLuu, btnTroLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Ánh xạ các view
        edtTenSP = findViewById(R.id.edtTenSP);
        edtLoaiSP = findViewById(R.id.edtLoaiSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtSoLuong = findViewById(R.id.edtSoLuong);

        tvNgaySX = findViewById(R.id.tvNgaySX);
        tvHanSD = findViewById(R.id.tvHanSD);

        btnLuu = findViewById(R.id.btnLuu);
        btnTroLai = findViewById(R.id.btnTroLai);

        // Xử lý sự kiện chọn ngày sản xuất
        tvNgaySX.setOnClickListener(v -> showDatePickerDialog(tvNgaySX));

        // Xử lý sự kiện chọn hạn sử dụng
        tvHanSD.setOnClickListener(v -> showDatePickerDialog(tvHanSD));

        // Xử lý sự kiện lưu sản phẩm
        btnLuu.setOnClickListener(v -> saveProduct());

        // Xử lý sự kiện quay lại
        btnTroLai.setOnClickListener(v -> finish());
    }

    /**
     * Hàm hiển thị DatePickerDialog để chọn ngày
     */
    private void showDatePickerDialog(final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Định dạng ngày dd/MM/yyyy
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textView.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    /**
     * Hàm lưu sản phẩm mới
     */
    private void saveProduct() {
        String tenSP = edtTenSP.getText().toString().trim();
        String loaiSP = edtLoaiSP.getText().toString().trim();
        String giaSP = edtGiaSP.getText().toString().trim();
        String ngaySX = tvNgaySX.getText().toString().trim();
        String hanSD = tvHanSD.getText().toString().trim();
        String soLuong = edtSoLuong.getText().toString().trim();

        // Kiểm tra nhập đầy đủ thông tin
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
