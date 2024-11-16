package com.example.Quanly_thuvien.LOPDAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Quanly_thuvien.LOPPRODUCT.Sach;
import com.example.Quanly_thuvien.LOPPRODUCT.Top;
import com.example.Quanly_thuvien.SQLopenhelper.CreateData;

import java.util.ArrayList;
import java.util.List;

public class Top10Dao {
    CreateData createData;
    SQLiteDatabase liteDatabase;
    Context context;

    public Top10Dao(Context context) {
        this.context = context;
        createData = new CreateData(context);
        liteDatabase = createData.getWritableDatabase();
    }

    public List<Top> GetTop() {
        // gới hạn 10 kết quả từ trên xuống
        String sql = "SELECT maSach , COUNT(maSach) AS soLuong FROM PhieuMuon GROUP BY maSach ORDER BY soLuong DESC LIMIT 10";
        List<Top> list = new ArrayList<>();
        SachDao sachDao = new SachDao(context);
        Cursor cursor = liteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Top top = new Top();
            Sach sach = sachDao.getId(cursor.getString(cursor.getColumnIndex("maSach")));
            top.tensach= sach.getTens();
            top.soluong=(Integer.parseInt(cursor.getString(cursor.getColumnIndex("soLuong"))));
            list.add(top);
        }
        return list;
    }
//
//    public int GETTOP() {
//        String sql = "SELECT maSach , COUNT(maSach) AS soLuong FROM PhieuMuon GROUP BY maSach ORDER BY soLuong DESC LIMIT 10";
//        Cursor cursor = liteDatabase.rawQuery(sql, null);
//        cursor.moveToFirst();
//        return cursor.getInt(0);
//    }
}
