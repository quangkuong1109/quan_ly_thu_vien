package com.example.Quanly_thuvien.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Quanly_thuvien.LOPPRODUCT.NhanVien;
import com.example.Quanly_thuvien.LOPDAO.NVDao;
import com.example.Quanly_thuvien.R;

import org.jetbrains.annotations.NotNull;

public class DoiPass extends Fragment {
    @Nullable
    @org.jetbrains.annotations.Nullable
    //khởi tạo giao diện fragment
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doipass_fragment, null, false);
    }
    //khởi tạo các biến
    NVDao nvdao;
    EditText ed_passnew, ed_passold, ed_passen;
    Button btn_save, btn_clear;
    NhanVien nhanVien;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ed_passold = view.findViewById(R.id.edpassold);//mk cũ
        ed_passnew = view.findViewById(R.id.edPassnew);//mk mới
        ed_passen = view.findViewById(R.id.edEnterpass);//mk nhập lại
        btn_clear = view.findViewById(R.id.btn_clear);//làm trống ô nhập
        btn_save = view.findViewById(R.id.btn_save);//luu
        nvdao = new NVDao(getActivity());//NVDAO
        String user = getActivity().getIntent().getStringExtra("admintion");
        nhanVien = nvdao.getId(user);//lấy tên user
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_passold.setText("");
                ed_passnew.setText("");
                ed_passen.setText("");
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkmk() > 0) {
                    nhanVien.setMaKhau(ed_passnew.getText().toString());
                    int kq = nvdao.Thaypass(nhanVien);
                    if (kq > 0) {
                        Toast.makeText(getActivity(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        ed_passold.setText("");
                        ed_passnew.setText("");
                        ed_passen.setText("");
                    }else {
                        Toast.makeText(getActivity(),"Thay đổi mật khẩu thất bại",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public int checkmk() {
        int check = 1;
        if (ed_passold.getText().length() == 0 || ed_passnew.getText().length() == 0 || ed_passen.getText().length() == 0) {
            Toast.makeText(getActivity(), "Hãy điền đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
            check = -1;
        } else {
            String passold = nhanVien.getMaKhau();
            String passnew = ed_passnew.getText().toString();
            String passent = ed_passen.getText().toString();
            if (!passold.equals(ed_passold.getText().toString())) {
                Toast.makeText(getActivity(), "Sai mật khẩu cũ", Toast.LENGTH_SHORT).show();
                check = -1;
            }
            if (!passnew.equals(passent)) {
                Toast.makeText(getActivity(), "Nhập lại mật khẩu không giống", Toast.LENGTH_SHORT).show();
                check = -1;
            }
        }
        return check;
    }
}
