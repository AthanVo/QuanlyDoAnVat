package com.example.quanlydoanvat.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditActivity extends AppCompatActivity {

    Spinner spnLoaiSP;
    EditText edtTenSP, edtGiaSP, edtSoLuong;
    TextView tvNgaySX, tvHanSD;
    Button btnSave, btnDelete, btnTroLai;

    private Product currentProduct;
    private String productId;
    private DatabaseReference databaseReference;
    private List<String> loaiSanPhamList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        spnLoaiSP = findViewById(R.id.spnLoaiSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaSP = findViewById(R.id.edtGiaSP);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        tvNgaySX = findViewById(R.id.tvNgaySX);
        tvHanSD = findViewById(R.id.tvHanSD);
        btnSave = findViewById(R.id.btnLuu);
        btnDelete = findViewById(R.id.btnXoa);
        btnTroLai = findViewById(R.id.btnTroLai);

        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // **Tạo danh sách loại sản phẩm**
        loaiSanPhamList = new ArrayList<>();
        loaiSanPhamList.add("Đồ ăn vặt");
        loaiSanPhamList.add("Đồ ngọt");
        loaiSanPhamList.add("Đồ cay");
        loaiSanPhamList.add("Đồ mặn");
        loaiSanPhamList.add("Nước uống");

        // **Gán danh sách vào Spinner**
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loaiSanPhamList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoaiSP.setAdapter(adapter);

        // **Nhận dữ liệu từ Intent**
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            productId = intent.getStringExtra("id");
            String tenSP = intent.getStringExtra("tenSP");
            String loaiSP = intent.getStringExtra("loaiSP");
            double giaSP = intent.getDoubleExtra("giaSP", 0);
            String ngaySX = intent.getStringExtra("ngaySX");
            String hanSD = intent.getStringExtra("hanSD");
            int soLuong = intent.getIntExtra("soLuong", 0);

            // **Hiển thị thông tin sản phẩm**
            edtTenSP.setText(tenSP);
            edtGiaSP.setText(String.valueOf(giaSP));
            edtSoLuong.setText(String.valueOf(soLuong));
            tvNgaySX.setText(ngaySX);
            tvHanSD.setText(hanSD);

            // **Chọn loại sản phẩm trong Spinner**
            int position = loaiSanPhamList.indexOf(loaiSP);
            if (position != -1) {
                spnLoaiSP.setSelection(position);
            }

            currentProduct = new Product(productId, tenSP, loaiSP, giaSP, ngaySX, hanSD, soLuong);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSave.setOnClickListener(v -> updateProduct());
        btnDelete.setOnClickListener(v -> deleteProduct());
        btnTroLai.setOnClickListener(v -> finish());
    }

    /**
     * Hiển thị DatePickerDialog để chọn ngày
     */
    private void showDatePickerDialog(final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    textView.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    /**
     * **Cập nhật sản phẩm đã có**
     */
    private void updateProduct() {
        if (productId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm để cập nhật!", Toast.LENGTH_SHORT).show();
            return;
        }

        String tenSP = edtTenSP.getText().toString().trim();
        String loaiSP = spnLoaiSP.getSelectedItem().toString(); // Lấy loại sản phẩm từ Spinner
        String giaSP = edtGiaSP.getText().toString().trim();
        String ngaySX = tvNgaySX.getText().toString().trim();
        String hanSD = tvHanSD.getText().toString().trim();
        String soLuong = edtSoLuong.getText().toString().trim();

        if (TextUtils.isEmpty(tenSP) || TextUtils.isEmpty(giaSP) ||
                TextUtils.isEmpty(ngaySX) || TextUtils.isEmpty(hanSD) || TextUtils.isEmpty(soLuong)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double gia = Double.parseDouble(giaSP);
            int sl = Integer.parseInt(soLuong);

            currentProduct.setTenSP(tenSP);
            currentProduct.setLoaiSP(loaiSP);
            currentProduct.setGiaSP(gia);
            currentProduct.setNgaySX(ngaySX);
            currentProduct.setHanSD(hanSD);
            currentProduct.setSoLuong(sl);

            databaseReference.child(productId).setValue(currentProduct)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddEditActivity.this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddEditActivity.this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá hoặc số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * **Xóa sản phẩm**
     */
    private void deleteProduct() {
        if (productId != null) {
            databaseReference.child(productId).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddEditActivity.this, "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddEditActivity.this, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Lỗi: Không thể xóa sản phẩm!", Toast.LENGTH_SHORT).show();
        }
    }
}
