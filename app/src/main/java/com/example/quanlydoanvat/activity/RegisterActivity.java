package com.example.quanlydoanvat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar; // Thêm ProgressBar
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextTaiKhoan, editTextMatKhau, editTextNhapLaiMatKhau;
    Button btnDangKyTK, btnQuayLai;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar; // Khai báo ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextTaiKhoan = findViewById(R.id.editTextTaiKhoan);
        editTextMatKhau = findViewById(R.id.editTextMatKhau);
        editTextNhapLaiMatKhau = findViewById(R.id.editTextNhapLaiMatKhau);
        btnDangKyTK = findViewById(R.id.btnDangKyTk);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        progressBar = findViewById(R.id.progressBar); // Ánh xạ ProgressBar

        btnDangKyTK.setOnClickListener(v -> {
            String taiKhoan = editTextTaiKhoan.getText().toString().trim();
            String matKhau = editTextMatKhau.getText().toString().trim();
            String nhapLaiMatKhau = editTextNhapLaiMatKhau.getText().toString().trim();

            if (taiKhoan.isEmpty() || matKhau.isEmpty() || nhapLaiMatKhau.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(taiKhoan).matches()) { // Kiểm tra định dạng email
                Toast.makeText(RegisterActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            } else if (!matKhau.equals(nhapLaiMatKhau)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar
                mAuth.createUserWithEmailAndPassword(taiKhoan, matKhau)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE); // Ẩn ProgressBar

                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Exception e = task.getException();
                                String errorMessage = "Đăng ký thất bại: ";

                                if (e instanceof FirebaseAuthUserCollisionException) {
                                    errorMessage += "Email đã tồn tại!";
                                } else if (e instanceof FirebaseAuthWeakPasswordException) {
                                    errorMessage += "Mật khẩu yếu (ít nhất 6 ký tự)!";
                                } else {
                                    errorMessage += e.getMessage(); // Lỗi chung
                                }

                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnQuayLai.setOnClickListener(v -> finish());
    }
}