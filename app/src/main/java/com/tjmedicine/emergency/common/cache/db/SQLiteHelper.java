package com.tjmedicine.emergency.common.cache.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjmedicine.emergency.EmergencyApplication;


/**
 * @author QiZai
 * @desc
 * @date 2018/6/4 11:15
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper instance = getInstance();
    private SQLiteDatabase mWritableDatabase;
    public UartDataDB mUartDB;

    private SQLiteHelper() {
        super(EmergencyApplication.getContext(), "UART_DATA.db", null, 1);
        mUartDB = new UartDataDB();
    }

    public static SQLiteHelper getInstance() {
        if (instance == null) {
            instance = new SQLiteHelper();
        }
        return instance;
    }

    /**
     * 获取数据库对象实例
     *
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getDBInstance() {
        if (mWritableDatabase == null)
            return mWritableDatabase = this.getWritableDatabase();
        return mWritableDatabase;
    }

    /**
     * 关闭数据库
     */
    public void closeDatabase() {
        if (mWritableDatabase != null)
            mWritableDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(mUartDB.createTableSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
