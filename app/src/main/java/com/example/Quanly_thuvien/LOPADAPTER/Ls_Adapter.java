package com.example.Quanly_thuvien.LOPADAPTER;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Quanly_thuvien.LOPPRODUCT.LoaiSach;
import com.example.Quanly_thuvien.LOPDAO.LoaiSachDao;
import com.example.Quanly_thuvien.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Ls_Adapter extends RecyclerView.Adapter<Ls_Adapter.LoaiSachhoder> {
    Context context;
    List<LoaiSach> sachList;
    LoaiSachDao loDao;

    public Ls_Adapter(Context context, List<LoaiSach> sachList, LoaiSachDao lsDao) {
        this.context = context;
        this.sachList = sachList;
        this.loDao = lsDao;
        // commit
    }

    @NonNull
    @NotNull
    @Override
    public LoaiSachhoder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_loaisach, parent, false);
        return new LoaiSachhoder(view);
    }

    // ok
    @Override
    public void onBindViewHolder(@NonNull @NotNull LoaiSachhoder holderls, int position) {
        LoaiSach loaiSach = sachList.get(position);
        if (loaiSach == null) {
            return;
        } else {
            holderls.tv_IDls.setText("Mã Loại Sách: " + loaiSach.getMaLS());
            holderls.tv_tenls.setText("Tên Loại Sách: " + loaiSach.getTenLS());
            holderls.tv_nhacc.setText("Nhà Cung Cấp: " + loaiSach.getNhacc());
        }

        holderls.imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Xóa loại sách");
                builder.setIcon(R.drawable.ic_baseline_delete_forever_24_red);
                builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loDao = new LoaiSachDao(context);
                        long kq = loDao.DELETELS(loaiSach);
                        if (kq > 0) {
                            // Sử dụng context chính xác
                            MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), R.raw.bubbles_bursting);
                            mp.start();
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    // Giải phóng MediaPlayer sau khi hoàn thành phát
                                    mp.release();
                                }
                            });
                            sachList.clear();
                            sachList.addAll(loDao.GETLS());
                            // load dữ liệu
                            notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(context.getApplicationContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context.getApplicationContext(), "Xóa Thất Bại", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        holderls.imgdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.custom_edit_loaisach, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(view);
                builder.setIcon(R.drawable.ic_baseline_edit_24);
                builder.setTitle("Sửa Loại Sách");
                EditText ed_loasc = (EditText) view.findViewById(R.id.ed_editls);
                EditText ed_ncc = (EditText) view.findViewById(R.id.edit_ncc);
                ed_loasc.setText(loaiSach.getTenLS());
                ed_ncc.setText(loaiSach.getNhacc());
                builder.setCancelable(true);
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (loaiSach.getTenLS().equals(ed_loasc.getText().toString()) && loaiSach.getNhacc().equals(ed_ncc.getText().toString())) {
                            Toast.makeText(context.getApplicationContext(), "Không Có Gì Thay Đổi \n   Sửa Thất Bại!", Toast.LENGTH_SHORT).show();

                        } else {
                            loDao = new LoaiSachDao(context);
                            loaiSach.setTenLS(ed_loasc.getText().toString());
                            loaiSach.setNhacc(ed_ncc.getText().toString());
                            long kq = loDao.UPDATELS(loaiSach);
                            if (kq > 0) {
                                // Sử dụng context chính xác
                                MediaPlayer mp = MediaPlayer.create(context.getApplicationContext(), R.raw.new_notification);
                                mp.start();
                                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        // Giải phóng MediaPlayer sau khi hoàn thành phát
                                        mp.release();
                                    }
                                });
                                sachList.clear();
                                sachList.addAll(loDao.GETLS());
                                notifyDataSetChanged();
                                ed_loasc.setText("");
                                ed_ncc.setText("");
                                dialog.dismiss();
                                Toast.makeText(context.getApplicationContext(), "Sửa Thành Công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context.getApplicationContext(), "Sửa Thất Bại", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (sachList != null) {
            return sachList.size();
        }
        return 0;
    }

    public class LoaiSachhoder extends RecyclerView.ViewHolder {
        TextView tv_IDls, tv_tenls, tv_nhacc;
        ImageView imgdel, imgdit;
        CardView cardView;

        public LoaiSachhoder(@NonNull @NotNull View view) {
            super(view);
            tv_IDls = (TextView) view.findViewById(R.id.tv_id_loaisach);
            tv_tenls = (TextView) view.findViewById(R.id.tv_ten_loaisach);
            tv_nhacc = (TextView) view.findViewById(R.id.tv_nhaccc);
            imgdel = (ImageView) view.findViewById(R.id.imgdells);
            imgdit = (ImageView) view.findViewById(R.id.imgditls);
            cardView = view.findViewById(R.id.cns_ls);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.transition);
            cardView.setAnimation(animation);
        }
    }
}
