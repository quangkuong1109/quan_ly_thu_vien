package com.example.Quanly_thuvien.LOPADAPTER;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Quanly_thuvien.LOPDAO.LoaiSachDao;
import com.example.Quanly_thuvien.LOPDAO.SachDao;
import com.example.Quanly_thuvien.LOPPRODUCT.LoaiSach;
import com.example.Quanly_thuvien.LOPPRODUCT.Sach;
import com.example.Quanly_thuvien.R;
import com.example.Quanly_thuvien.SPINERADAPTER.LoaiSachSpiner;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class S_Adapter extends RecyclerView.Adapter<S_Adapter.SachHoder> implements Filterable {
    Context context;
    List<Sach> list;
    SachDao dao;
    int ms, mst;
    ArrayList<LoaiSach> loaiSaches;
    LoaiSachDao loaiSachDao;
    List<Sach> mlistOld;

    public S_Adapter(Context context, List<Sach> list, SachDao dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
        this.mlistOld = list;
    }

    @NonNull
    @NotNull
    @Override
    public SachHoder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_sach, parent, false);
        return new SachHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SachHoder holder, int position) {
        Sach sach = list.get(position);
        if (sach == null) {
            return;
        }
        if (sach.getTens().startsWith("Không tìm thấy kết quả")) {
            holder.tv_ms.setText("");
            holder.tv_mls.setText("");
            holder.tv_tens.setText(sach.getTens());
            holder.tv_tacgia.setText("");
            holder.tv_gias.setText("");

            //Ẩn các biểu tượng
            holder.img_dels.setVisibility(View.GONE);
            holder.img_edits.setVisibility(View.GONE);
            holder.tv_tens.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);  // Ẩn biểu tượng quyển sách
        } else {
            holder.img_dels.setVisibility(View.VISIBLE);
            holder.img_edits.setVisibility(View.VISIBLE);

            String tenLoai;
            try {
                LoaiSachDao loaiSachDao = new LoaiSachDao(context);
                LoaiSach loaiSach = loaiSachDao.getId(String.valueOf(sach.getMals()));
                tenLoai = loaiSach.getTenLS();
            } catch (Exception e) {
                tenLoai = "Đã xóa loại sách";
            }

            holder.tv_ms.setText("Mã Sách: " + sach.getMas() + "");
            holder.tv_mls.setText("Loại Sách: " + tenLoai);
            holder.tv_tens.setText("Tên Sách: " + sach.getTens());
            holder.tv_tacgia.setText("Tác Giả: " + sach.getTacgia());
            Locale locale = new Locale("nv", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String tien = numberFormat.format(sach.getGias());
            holder.tv_gias.setText("Giá Sách: " + tien);
        }
        holder.img_dels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Xóa thông tin sách");
                builder.setIcon(R.drawable.ic_baseline_delete_forever_24_red);
                builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
                builder.setCancelable(true);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao = new SachDao(context);
                        long kq = dao.DELETES(sach);
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
                            list.clear();
                            list.addAll(dao.GETS());
                            Toast.makeText(context.getApplicationContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            dialog.cancel();
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
        holder.img_edits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);//tạo cacs đối tượng view
                View view = inflater.inflate(R.layout.custom_edit_sach, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(view);
                builder.setIcon(R.drawable.ic_baseline_edit_24);
                builder.setTitle("Sửa thông tin sách");
                EditText ed_teneds = (EditText) view.findViewById(R.id.tensached);
                Spinner spneds = (Spinner) view.findViewById(R.id.spin_lsached);
                EditText ed_giaeds = (EditText) view.findViewById(R.id.giasached);
                ed_teneds.setText(sach.getTens());
                ed_giaeds.setText(Integer.toString(sach.getGias()));
                loaiSaches = new ArrayList<>();
                loaiSachDao = new LoaiSachDao(view.getContext());
                loaiSaches = (ArrayList<LoaiSach>) loaiSachDao.GETLS();
                LoaiSachSpiner spiner = new LoaiSachSpiner(view.getContext(), loaiSaches);
                spneds.setAdapter(spiner);
                spneds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ms = loaiSaches.get(position).getMaLS();
                        Toast.makeText(view.getContext(), "Chọn: " + loaiSaches.get(position).getTenLS(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                mst = 0;
                for (int i = 0; i < loaiSaches.size(); i++) {
                    if (sach.getMals() == loaiSaches.get(i).getMaLS()) {
                        mst = i;
                    }
                }
                spneds.setSelection(mst);
                builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (sach.getTens().equals(ed_teneds.getText().toString()) && sach.getGias() == Integer.parseInt(ed_giaeds.getText().toString())
                                && sach.getMals() == mst) {
                            Toast.makeText(context.getApplicationContext(), "Không Có Gì Thay Đổi \n   Sửa Thất Bại!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                int giaThue = Integer.parseInt(ed_giaeds.getText().toString());
                                dao = new SachDao(context);
                                sach.setTens(ed_teneds.getText().toString());
                                sach.setGias(giaThue);
                                sach.setMals(ms);
                                long kq = dao.UPDATES(sach);
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
                                    list.clear();
                                    list.addAll(dao.GETS());
                                    Toast.makeText(view.getContext(), "Sửa Thành Công", Toast.LENGTH_SHORT).show();
                                    ed_teneds.setText("");
                                    ed_giaeds.setText("");
                                    spneds.setSelection(0);
                                    notifyDataSetChanged();
                                    dialog.cancel();
                                } else {
                                    Toast.makeText(view.getContext(), "Sửa Thất Bại", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(view.getContext(), "Giá thuê phải là số", Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {//trả về so luong trong list
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                Sach noResult = new Sach();
                if (strSearch.isEmpty()) {
                    list = mlistOld;
                } else {
                    List<Sach> filteredList = new ArrayList<>();
                    for (Sach sach : mlistOld) {
                        if (sach.getTacgia().toLowerCase().contains(strSearch.toLowerCase()) ||
                                String.valueOf(sach.getMas()).contains(strSearch) ||
                                sach.getTens().toLowerCase().contains(strSearch.toLowerCase())) {
                            filteredList.add(sach);
                        }
                        // Nếu không có sách nào khớp, thêm đối tượng "Không tìm thấy kết quả"
                        if (filteredList.isEmpty()) {
                            noResult.setTens("Không tìm thấy kết quả nào cho \"" + strSearch + "\"");
                            filteredList.add(noResult);
                        }
                    }
                    list = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<Sach>) results.values;
                notifyDataSetChanged();
            }
        };

    }

    public class SachHoder extends RecyclerView.ViewHolder {
        TextView tv_ms, tv_mls, tv_tens, tv_gias, tv_tacgia;
        ImageView img_dels, img_edits;
        ConstraintLayout cns_lays;

        public SachHoder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_ms = itemView.findViewById(R.id.tv_masach);
            tv_mls = itemView.findViewById(R.id.tv_maloais);
            tv_tens = itemView.findViewById(R.id.tv_tensach);
            tv_gias = itemView.findViewById(R.id.tv_giasach);
            tv_tacgia = itemView.findViewById(R.id.tv_tacgia);
            img_dels = itemView.findViewById(R.id.img_deltsach);
            img_edits = itemView.findViewById(R.id.img_editsach);
            cns_lays = itemView.findViewById(R.id.conss);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.transition);
            cns_lays.setAnimation(animation);
        }
    }
}
