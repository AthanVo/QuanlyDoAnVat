package com.example.quanlydoanvat.activity;

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

public class AddEditActivity extends AppCompatActivity {

    EditText edtTenSP, edtLoaiSP, edtGiaSP, edtSoLuong;
    TextView tvNgaySX, tvHanSD;
    Button btnSave, btnTroLai;

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        edtTenSP = findViewById(R.id.edtTenSP);
        edtLoaiSP = findViewById(R.id.edtLoaiSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        tvNgaySX = findViewById(R.id.tvNgaySX);
        tvHanSD = findViewById(R.id.tvHanSD);

        btnSave = findViewById(R.id.btnLuu);
        btnTroLai = findViewById(R.id.btnTroLai);

        // Xử lý sự kiện chọn ngày
        tvNgaySX.setOnClickListener(v -> showDatePickerDialog(tvNgaySX));
        tvHanSD.setOnClickListener(v -> showDatePickerDialog(tvHanSD));

        // Nhận dữ liệu sản phẩm từ Intent (nếu có)
        if (getIntent() != null) {
            currentProduct = new Product(
                    getIntent().getStringExtra("tenSP"),
                    getIntent().getStringExtra("loaiSP"),
                    getIntent().getDoubleExtra("giaSP", 0),
                    getIntent().getStringExtra("ngaySX"),
                    getIntent().getStringExtra("hanSD"),
                    getIntent().getIntExtra("soLuong", 0)
            );

            edtTenSP.setText(currentProduct.getTenSP());
            edtLoaiSP.setText(currentProduct.getLoaiSP());
            edtGiaSP.setText(String.valueOf(currentProduct.getGiaSP()));
            tvNgaySX.setText(currentProduct.getNgaySX());
            tvHanSD.setText(currentProduct.getHanSD());
            edtSoLuong.setText(String.valueOf(currentProduct.getSoLuong()));
        }

        btnSave.setOnClickListener(v -> saveProduct());
        btnTroLai.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    textView.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void saveProduct() {
        String tenSP = edtTenSP.getText().toString().trim();
        String loaiSP = edtLoaiSP.getText().toString().trim();
        String giaSP = edtGiaSP.getText().toString().trim();
        String ngaySX = tvNgaySX.getText().toString().trim();
        String hanSD = tvHanSD.getText().toString().trim();
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

            if (currentProduct != null) {
                currentProduct.setTenSP(tenSP);
                currentProduct.setLoaiSP(loaiSP);
                currentProduct.setGiaSP(gia);
                currentProduct.setNgaySX(ngaySX);
                currentProduct.setHanSD(hanSD);
                currentProduct.setSoLuong(sl);
            } else {
                currentProduct = new Product(tenSP, loaiSP, gia, ngaySX, hanSD, sl);
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedProduct", currentProduct);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Lưu sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }
}
