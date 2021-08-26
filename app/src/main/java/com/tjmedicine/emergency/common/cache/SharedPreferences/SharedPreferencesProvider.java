package com.tjmedicine.emergency.common.cache.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * @author QiZai
 * @desc
 * @date 2018/4/12
 */

public class SharedPreferencesProvider {

    private static SharedPreferences sharedPreferences = null;

    private SharedPreferencesProvider() {}

    public static SharedPreferences getInstance(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
