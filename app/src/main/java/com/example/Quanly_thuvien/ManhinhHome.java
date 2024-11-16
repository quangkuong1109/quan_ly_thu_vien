package com.example.Quanly_thuvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.Quanly_thuvien.Login.Login;

public class ManhinhHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinh_home);

    }
    public void loo(View view) {
        Intent intent = new Intent(ManhinhHome.this, Login.class);
        startActivity(intent);
    }
}