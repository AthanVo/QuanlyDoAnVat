package com.example.quanlydoanvat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydoanvat.R;
import com.example.quanlydoanvat.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Product> productList = new ArrayList<>();
    private EditText editTextTaiKhoan, editTextMatKhau;
    private Button btnDangNhap, btnDangKy;
    private CheckBox checkBoxLuu;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view
        editTextTaiKhoan = findViewById(R.id.editTextTaiKhoan);
        editTextMatKhau = findViewById(R.id.editTextMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);
        checkBoxLuu = findViewById(R.id.checkBoxLuu);
        progressBar = findViewById(R.id.progressBar);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        // Kiểm tra trạng thái đăng nhập
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Người dùng đã đăng nhập, chuyển đến màn hình chính
            startActivity(new Intent(MainActivity.this, ProductListActivity.class));
            finish();
        }

        // Tự động điền thông tin đăng nhập nếu đã lưu
        if (sharedPreferences.getBoolean("remember", false)) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");
            editTextTaiKhoan.setText(savedUsername);
            editTextMatKhau.setText(savedPassword);
            checkBoxLuu.setChecked(true);
        }

        // Xử lý sự kiện đăng nhập
        btnDangNhap.setOnClickListener(v -> {
            String email = editTextTaiKhoan.getText().toString().trim();
            String password = editTextMatKhau.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ẩn bàn phím ảo
            hideKeyboard();

            // Hiển thị ProgressBar
            progressBar.setVisibility(View.VISIBLE);

            // Đăng nhập với Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Email đã xác thực, cho phép đăng nhập
                                Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                                // Lưu thông tin đăng nhập nếu người dùng chọn "Lưu mật khẩu"
                                if (checkBoxLuu.isChecked()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", email);
                                    editor.putString("password", password);
                                    editor.putBoolean("remember", true);
                                    editor.apply();
                                } else {
                                    sharedPreferences.edit().clear().apply();
                                }

                                // Khởi tạo danh sách sản phẩm và chuyển đến màn hình chính
                                initProductList();
                                startActivity(new Intent(MainActivity.this, ProductListActivity.class));
                                finish();
                            } else {
                                // Email chưa xác thực
                                Toast.makeText(MainActivity.this, "Vui lòng xác thực email trước khi đăng nhập!", Toast.LENGTH_SHORT).show();
                                mAuth.signOut(); // Đăng xuất người dùng
                            }
                        } else {
                            // Đăng nhập thất bại
                            Exception e = task.getException();
                            String errorMessage = "Đăng nhập thất bại: ";

                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                errorMessage += "Email hoặc mật khẩu không hợp lệ!";
                            } else if (e instanceof FirebaseAuthInvalidUserException) {
                                errorMessage += "Tài khoản không tồn tại!";
                            } else {
                                errorMessage += e.getMessage();
                            }

                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Xử lý sự kiện đăng ký
        btnDangKy.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });


    }

    // Khởi tạo danh sách sản phẩm
    private void initProductList() {
        productList.add(new Product("Snack Bò Khô", "Đồ ăn vặt", 25000, "01/01/2024", "01/01/2025", 20));
        productList.add(new Product("Bim Bim Cay", "Đồ ăn vặt", 15000, "02/01/2024", "02/01/2025", 15));
        productList.add(new Product("Kẹo Mút Trái Cây", "Kẹo", 5000, "03/01/2024", "03/01/2025", 50));
        productList.add(new Product("Bánh Quy Socola", "Bánh", 30000, "04/01/2024", "04/01/2025", 10));
    }


    // Ẩn bàn phím ảo
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}