package com.ps.xh.facefile.utils;


import android.content.Context;
import android.support.annotation.NonNull;

/**
 * sharePreferences 工具类
 */
public class SPUtils {
    /**
     * 保存文件
     * @param name  文件名
     * @param key   key
     * @param value value
     */
    public static void save(@NonNull Context context, String name, String key, String value) {
        context.getApplicationContext().getSharedPreferences(name, 0).edit().putString(key, value).apply();
    }
    /**
     * 读取文件
     * @param name 文件名
     * @param key key
     * 如果没有返回空  -- ""
     */
    public static String read(@NonNull Context context, String name, String key) {
        return context.getApplicationContext().getSharedPreferences(name, 0).getString(key, "");
    }
    /**
     * 删除文件
     * @param name 文件名
     * @param key key
     */
    public static void remove(@NonNull Context context, String name, String key) {
        context.getApplicationContext().getSharedPreferences(name, 0).edit().remove(key).apply();
    }

}
