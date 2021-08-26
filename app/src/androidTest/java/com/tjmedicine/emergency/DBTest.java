package com.tjmedicine.emergency;


import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.tjmedicine.emergency.common.cache.db.SQLiteHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DBTest{


    @Test
    public void test2(){
        System.out.println("1111111111111111111111");
        SQLiteHelper.getInstance();
        System.out.println("执行数据");
    }
}
