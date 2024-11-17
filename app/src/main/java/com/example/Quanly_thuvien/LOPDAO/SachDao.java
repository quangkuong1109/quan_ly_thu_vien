package com.example.Quanly_thuvien.LOPDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Quanly_thuvien.LOPPRODUCT.Sach;
import com.example.Quanly_thuvien.SQLopenhelper.CreateData;

import java.util.ArrayList;
import java.util.List;

public class SachDao {
    SQLiteDatabase sqLiteDatabase;
    CreateData createData;

    public SachDao(Context context) {
        createData = new CreateData(context);
        sqLiteDatabase = createData.getWritableDatabase();
    }

    public long ADDS(Sach sach) {
        ContentValues values = new ContentValues();
        values.put(Sach.COL_NAME_MALS, sach.getMals());
        values.put(Sach.COL_NAME_TENS, sach.getTens());
        values.put(Sach.COL_NAME_GIAS, sach.getGias());
        values.put(Sach.COL_NAME_tacgia, sach.getTacgia());
        return sqLiteDatabase.insert(Sach.TB_NAME, null, values);
    }

    public int DELETES(Sach sach) {
        return sqLiteDatabase.delete(Sach.TB_NAME, "maSach=?", new String[]{String.valueOf(sach.getMas())});
    }

    public int UPDATES(Sach sach) {
        ContentValues values = new ContentValues();
        values.put(Sach.COL_NAME_MALS, sach.getMals());
        values.put(Sach.COL_NAME_TENS, sach.getTens());
        values.put(Sach.COL_NAME_GIAS, sach.getGias());
        values.put(Sach.COL_NAME_tacgia, sach.getTacgia());
        return sqLiteDatabase.update(Sach.TB_NAME, values, "maSach=?", new String[]{String.valueOf(sach.getMas())});
    }

    public List<Sach> GETS() {
        String dl = "SELECT * FROM Sach";
        List<Sach> list = getdata(dl);
        return list;
    }

    public boolean KiemTraTonTai(String tenSach, int maLoaiSach) {
        String sql = "SELECT COUNT(*) FROM Sach WHERE tens = ? AND mals = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{tenSach, String.valueOf(maLoaiSach)});
        boolean tontai = false;
        if (cursor.moveToFirst()) {
            tontai = cursor.getInt(0) > 0; // Nếu có ít nhất một kết quả, sách đã tồn tại
        }
        cursor.close(); // Đảm bảo đóng Cursor sau khi sử dụng
        return tontai;
    }



    public Sach getId(String id) {
        String sql = "SELECT * FROM Sach WHERE maSach=?";
        List<Sach> list = getdata(sql, id);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null; // Hoặc xử lý khác nếu không tìm thấy sách
    }


    private List<Sach> getdata(String dl, String... Arays /* có hoặc không nhiều phần tử*/) {
        List<Sach> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(dl, Arays);
        while (cursor.moveToNext()) {
            Sach sach = new Sach();
            sach.setMas(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Sach.COL_NAME_MAS))));
            sach.setMals(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Sach.COL_NAME_MALS))));
            sach.setTens(cursor.getString(cursor.getColumnIndex(Sach.COL_NAME_TENS)));
            sach.setTacgia(cursor.getString(cursor.getColumnIndex(Sach.COL_NAME_tacgia)));
            sach.setGias(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Sach.COL_NAME_GIAS))));
            list.add(sach);
        }
        return list;
    }
}
