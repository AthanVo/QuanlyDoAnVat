package com.example.quanlydoanvat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Product> productList = new ArrayList<>();
    EditText editTextTaiKhoan, editTextMatKhau;
    Button btnDangNhap, btnDangKy;
    CheckBox checkBoxLuu;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTaiKhoan = findViewById(R.id.editTextTaiKhoan);
        editTextMatKhau = findViewById(R.id.editTextMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);
        checkBoxLuu = findViewById(R.id.checkBoxLuu);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Kiểm tra và tự động điền tài khoản nếu đã lưu
        if (sharedPreferences.getBoolean("remember", false)) {
            editTextTaiKhoan.setText(sharedPreferences.getString("username", ""));
            editTextMatKhau.setText(sharedPreferences.getString("password", ""));
            checkBoxLuu.setChecked(true);
        }

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextTaiKhoan.getText().toString().trim();
                String password = editTextMatKhau.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    String savedPassword = sharedPreferences.getString(username, "");
                    if (password.equals(savedPassword)) {
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Lưu thông tin nếu checkbox được chọn
                        if (checkBoxLuu.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putBoolean("remember", true);
                            editor.apply();
                        } else {
                            sharedPreferences.edit().clear().apply(); // Xóa thông tin nếu không lưu
                        }

                        initProductList(); // Khởi tạo danh sách sản phẩm mẫu
                        startActivity(new Intent(MainActivity.this, ProductListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void initProductList() {
        productList.add(new Product("Snack Bò Khô", "Đồ ăn vặt", 25000, "01/01/2024", "01/01/2025", 20));
        productList.add(new Product("Bim Bim Cay", "Đồ ăn vặt", 15000, "02/01/2024", "02/01/2025", 15));
        productList.add(new Product("Kẹo Mút Trái Cây", "Kẹo", 5000, "03/01/2024", "03/01/2025", 50));
        productList.add(new Product("Bánh Quy Socola", "Bánh", 30000, "04/01/2024", "04/01/2025", 10));
    }
}
