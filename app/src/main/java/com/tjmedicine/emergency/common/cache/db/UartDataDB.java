package com.tjmedicine.emergency.common.cache.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaojun
 * @desc
 * @date 2018/08/21 12:24
 */

public class UartDataDB implements IBaseDB {
    private final String TABLE_NAME = "UART_DATA";

    public final String KEY_ID = "id";
    public final String KEY_DATA = "data";
//    public final String KEY_NAME = "name";
//    public final String KEY_ROLE = "role";


    @Override
    public String createTableSql() {
        return "CREATE TABLE " + TABLE_NAME +
                " (" +
                KEY_ID + " bigint primary key," +
                KEY_DATA + " varchar(355)" +
//                KEY_NAME + " varchar(64)," +
//                KEY_ROLE + " varchar(64)" +
                ")";
    }

    /**
     * 查询数据
     * @param db
     * @return
     */
    public List<Map<String,Object>> queryUserInfo(SQLiteDatabase db, String like) {
        List<Map<String,Object>> dataList = new ArrayList<>();
        if (db != null) {
//            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = '" + like + "'";
            String sql = "SELECT * FROM " + TABLE_NAME ;
            Cursor cursor = db.rawQuery(sql, null);
            int i=0;
            while (cursor.moveToNext()) {
                i++;
                Map<String,Object> data = new HashMap<>();
                data.put(KEY_ID, cursor.getString(cursor.getColumnIndex(KEY_ID)));
                data.put(KEY_DATA, cursor.getString(cursor.getColumnIndex(KEY_DATA)));
//                data.put(KEY_NAME, cursor.getString(cursor.getColumnIndex(KEY_NAME)));
//                data.put(KEY_ROLE, cursor.getString(cursor.getColumnIndex(KEY_ROLE)));
                Logger.d("数据库中数据数量："+i);
                dataList.add(data);
            }
            cursor.close();
        }
        return dataList;
    }

    /**
     * 更新数据
     * @param db
     * @return
     */
    public void updateData(SQLiteDatabase db, Map<String, Object> info) {
        db.beginTransaction(); // 开启事务
        String sql = "UPDATE " + TABLE_NAME +
                " SET  " + KEY_DATA + "='" + info.get(KEY_DATA) +
//                "'," + KEY_NAME + "='" + info.get(KEY_NAME) +
//                "'," + KEY_ROLE + "='" + info.get(KEY_ROLE) +
                "' WHERE " + KEY_ID + " = " + info.get(KEY_ID);


        db.execSQL(sql);
        db.setTransactionSuccessful(); // 提交事务
        db.endTransaction(); // 关闭事务
    }

    /**
     * 插入数据
     *
     * @param db   数据库对象
     * @param info 工种集合
     */
    public void insertData(SQLiteDatabase db, Map<String, Object> info) {
        db.beginTransaction(); // 开启事务
        String sql = "INSERT OR REPLACE INTO " + TABLE_NAME +
                " values(" +
                info.get(KEY_ID) + ",'" +
//                info.get(KEY_DATA) + "','" +
//                info.get(KEY_NAME) + "','" +
                info.get(KEY_DATA) +
                "')"
         ;
        db.execSQL(sql);
        db.setTransactionSuccessful(); // 提交事务
        db.endTransaction(); // 关闭事务
    }


    /**
     * 删除表数据
     * thy
     *
     * @param db
     */
    public void removeAll(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
