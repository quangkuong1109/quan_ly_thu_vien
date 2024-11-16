package com.example.Quanly_thuvien.LOPDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Quanly_thuvien.LOPPRODUCT.LoaiSach;
import com.example.Quanly_thuvien.SQLopenhelper.CreateData;

import java.util.ArrayList;
import java.util.List;

public class LoaiSachDao {
    CreateData createData;
    SQLiteDatabase liteDatabase;

    public LoaiSachDao(Context context) {
        createData = new CreateData(context);
        liteDatabase = createData.getWritableDatabase();
    }

    public long ADDLS(LoaiSach loaiSach) {
        ContentValues values = new ContentValues();
        values.put(LoaiSach.COL_NAME_TENLS, loaiSach.getTenLS());
        values.put(LoaiSach.COL_NAME_NCC, loaiSach.getNhacc());
        return liteDatabase.insert(LoaiSach.TB_NAME, null, values);
    }

    public int UPDATELS(LoaiSach loaiSach) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoaiSach.COL_NAME_TENLS, loaiSach.getTenLS());
        contentValues.put(LoaiSach.COL_NAME_NCC, loaiSach.getNhacc());
        return liteDatabase.update(LoaiSach.TB_NAME, contentValues, "maLoai=?", new String[]{String.valueOf(loaiSach.getMaLS())});
    }

    public int DELETELS(LoaiSach loaiSach) {
        return liteDatabase.delete(LoaiSach.TB_NAME, "maLoai=?", new String[]{String.valueOf(loaiSach.getMaLS())});
    }

    public List<LoaiSach> GETLS() {
        String dl = "SELECT * FROM LoaiSach";
        List<LoaiSach> list = getdata(dl);
        return list;
    }

    public LoaiSach getId(String id) {
        String sql = "SELECT * FROM LoaiSach WHERE maLoai=?";
        List<LoaiSach> list = getdata(sql, id);
        return list.get(0);
    }

    private List<LoaiSach> getdata(String dl, String... Arays) {
        List<LoaiSach> list = new ArrayList<>();
        Cursor cursor = liteDatabase.rawQuery(dl, Arays);
        while (cursor.moveToNext()) {
            LoaiSach loaiSach = new LoaiSach();
            loaiSach.setMaLS(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LoaiSach.COL_NAME_MALS))));
            loaiSach.setTenLS(cursor.getString(cursor.getColumnIndex(LoaiSach.COL_NAME_TENLS)));
            loaiSach.setNhacc(cursor.getString(cursor.getColumnIndex(LoaiSach.COL_NAME_NCC)));
            list.add(loaiSach);
        }
        return list;

    }
}
