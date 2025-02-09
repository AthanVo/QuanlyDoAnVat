package com.example.quanlydoanvat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextTaiKhoan, editTextMatKhau, editTextNhapLaiMatKhau;
    Button btnDangKyTK, btnQuayLai;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        editTextTaiKhoan = findViewById(R.id.editTextTaiKhoan);
        editTextMatKhau = findViewById(R.id.editTextMatKhau);
        editTextNhapLaiMatKhau = findViewById(R.id.editTextNhapLaiMatKhau);
        btnDangKyTK = findViewById(R.id.btnDangKyTk);
        btnQuayLai = findViewById(R.id.btnQuayLai);

        btnDangKyTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextTaiKhoan.getText().toString().trim();
                String password = editTextMatKhau.getText().toString().trim();
                String confirmPassword = editTextNhapLaiMatKhau.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                } else {
                    if (sharedPreferences.contains(username)) {
                        Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(username, password);
                        editor.apply();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại trang đăng nhập
                    }
                }
            }
        });

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại MainActivity
            }
        });
    }
}
