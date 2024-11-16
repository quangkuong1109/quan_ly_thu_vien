package com.example.Quanly_thuvien.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.Quanly_thuvien.LOPDAO.NVDao;
import com.example.Quanly_thuvien.LOPPRODUCT.NhanVien;
import com.example.Quanly_thuvien.MainActivity;
import com.example.Quanly_thuvien.R;

public class Login extends AppCompatActivity {
    private AppCompatButton btn_login, btn_clear;
    private EditText ed_user, ed_pass;
    Intent intent;
    NVDao nvdao;
    CheckBox chk_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        btn_login = findViewById(R.id.login_btn);
        btn_clear = findViewById(R.id.btn_clear);
        ed_user = findViewById(R.id.ed_user);
        ed_pass = findViewById(R.id.ed_pass);
        chk_remember = findViewById(R.id.chk_remember);
        nvdao = new NVDao(this);
//        NhanVien nhanVien = nvdao.getUser("admin");
        nvdao.OPEN();
        if (nvdao.getUserName("admin") < 0) {//nếu bảng nhanvien không có tk admin thì sẽ tự tạo tài khoản admin với các thông tin tương ứng
            nvdao.ADDNV(new NhanVien("admin", "admin", "admin"));//tự động tạo admin nếu trong bảng nhân viên chưa có admin
        }
        // Đọc Sharepreferences
        SharedPreferences preferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        ed_user.setText(preferences.getString("USERNAME", ""));
        ed_pass.setText(preferences.getString("PASSWORD", ""));
        chk_remember.setChecked(preferences.getBoolean("REMEMBER", false));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklogin();
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

    }
    //|| (usered.equalsIgnoreCase("user") && passed.equalsIgnoreCase("user")
    public void checklogin() {
        String usered = ed_user.getText().toString();
        String passed = ed_pass.getText().toString();
        if (usered.isEmpty() || passed.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Tên đăng nhập không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {//nếu có tài khoản mật khẩu thỏa mãn hoặc so sánh tài khoản mật khẩu với admin
            if (nvdao.getlogin(usered, passed) > 0 || (usered.equalsIgnoreCase("admin") && passed.equalsIgnoreCase("admin"))) {
                Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                rememberUser(usered, passed, chk_remember.isChecked());
                startActivity(intent = new Intent(Login.this, MainActivity.class).putExtra("admintion", usered));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Đăng nhập thất bại ! " +
                        "\nSai tài khoản,mật khẩu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void rememberUser(String user, String pass, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);//tạo file SharePreference
        SharedPreferences.Editor editor = pref.edit();//lấy đối tượng editor để chỉnh sưửa dữ liệu trong SP
        if (!status) {//nếu khoong ấn checkbox
            // Xóa lưu trữ trước đó
            editor.clear();
        } else {
            // Lưu dữ liệu
            editor.putString("USERNAME", user);
            editor.putString("PASSWORD", pass);
            editor.putBoolean("REMEMBER", status);
        }
        // LƯu lại toàn bộ dữ liệu
        editor.commit();
    }

}
