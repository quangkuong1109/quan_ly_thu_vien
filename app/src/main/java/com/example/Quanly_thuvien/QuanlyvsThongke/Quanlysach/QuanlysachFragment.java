package com.example.Quanly_thuvien.QuanlyvsThongke.Quanlysach;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.Quanly_thuvien.LOPADAPTER.S_Adapter;
import com.example.Quanly_thuvien.LOPDAO.LoaiSachDao;
import com.example.Quanly_thuvien.LOPDAO.SachDao;
import com.example.Quanly_thuvien.LOPPRODUCT.LoaiSach;
import com.example.Quanly_thuvien.LOPPRODUCT.Sach;
import com.example.Quanly_thuvien.R;
import com.example.Quanly_thuvien.SPINERADAPTER.LoaiSachSpiner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class QuanlysachFragment extends Fragment {
    RecyclerView rcl_sach;
    FloatingActionButton flb_sach;
    ArrayList<LoaiSach> loaiSaches;
    int mals;
    LoaiSachDao loaiSachDao;
    LoaiSachSpiner sachSpiner;
    SachDao dao;
    SachViewModel model;
    S_Adapter adapter;
    SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sach, null);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcl_sach = (RecyclerView) view.findViewById(R.id.rcl_sach);
        flb_sach = (FloatingActionButton) view.findViewById(R.id.fbl_ads);
        dao = new SachDao(getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcl_sach.setLayoutManager(layoutManager);
        model = new ViewModelProvider(this).get(SachViewModel.class);
        model.getLiveData().observe(getViewLifecycleOwner(), new Observer<List<Sach>>() {
            @Override
            public void onChanged(List<Sach> saches) {//hiển thị dữ liệu lên fragment
                adapter = new S_Adapter(getActivity(), saches, dao);
                rcl_sach.setAdapter(adapter);
//                rcl_sach.setItemAnimator(new ScaleInAnimator());
//        //        adapter = new StudentAdapter(students, this);
//              rcl_sach.setAdapter(new ScaleInAnimationAdapter(adapter));
            }
        });
        searchView = view.findViewById(R.id.id_serch);
        searchView.setQueryHint("Nhập id, tên sách, tên tác giả, loại sách");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        flb_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.custom_add_sach, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);
                EditText ed_tens = (EditText) view.findViewById(R.id.tensach);
                Spinner spns = (Spinner) view.findViewById(R.id.spin_lsach);
                EditText ed_gias = (EditText) view.findViewById(R.id.giasach);
                EditText ed_tacgia = (EditText) view.findViewById(R.id.ed_tacgia);
                builder.setTitle("Thêm Sách");
                loaiSaches = new ArrayList<>();
                loaiSachDao = new LoaiSachDao(getContext());
                loaiSaches = (ArrayList<LoaiSach>) loaiSachDao.GETLS();
                sachSpiner = new LoaiSachSpiner(getActivity(), loaiSaches);
                spns.setAdapter(sachSpiner);
                spns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mals = loaiSaches.get(position).getMaLS();
                        Toast.makeText(getContext(), "Chọn" + loaiSaches.get(position).getTenLS(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ed_tens.getText().length() == 0 || ed_gias.getText().length() == 0 || ed_gias.getText().length() == 0) {
                            Toast.makeText(getContext(), "Bạn cần phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                int giaThue = Integer.parseInt(ed_gias.getText().toString());
                                Sach sach = new Sach();
                                sach.setTens(ed_tens.getText().toString());
                                sach.setTacgia(ed_tacgia.getText().toString());
                                sach.setGias(giaThue);
                                sach.setMals(mals);
                                long kq = dao.ADDS(sach);
                                if (kq > 0) {
                                    // Sử dụng context chính xác
                                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.new_notification);
                                    mp.start();
                                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            // Giải phóng MediaPlayer sau khi hoàn thành phát
                                            mp.release();
                                        }
                                    });
                                    Toast.makeText(getContext(), "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                                    ed_tens.setText("");
                                    ed_gias.setText("");
                                    ed_tacgia.setText("");
                                    spns.setSelection(0);
                                    model.getLiveData();
                                    adapter.notifyDataSetChanged();//cập nhật lại adapter để hiển thị
                                } else {
                                    Toast.makeText(getContext(), "Thêm sách thất bại", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Giá thuê phải là số", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}